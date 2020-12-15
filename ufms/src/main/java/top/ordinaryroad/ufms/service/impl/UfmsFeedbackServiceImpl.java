package top.ordinaryroad.ufms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.DateUtil;
import top.ordinaryroad.ufms.common.utils.JcsegUtil;
import top.ordinaryroad.ufms.common.utils.OffsetBasedPageRequest;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import top.ordinaryroad.ufms.dao.UfmsFeedbackDao;
import top.ordinaryroad.ufms.entity.SysUser;
import top.ordinaryroad.ufms.entity.UfmsFeedback;
import top.ordinaryroad.ufms.entity.UfmsProduct;
import top.ordinaryroad.ufms.service.UfmsFeedbackService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户反馈服务实现类
 *
 * @author qq1962247851
 * @date 2020/11/17 9:55
 **/
@Service
public class UfmsFeedbackServiceImpl implements UfmsFeedbackService {

    private final UfmsFeedbackDao dao;

    public UfmsFeedbackServiceImpl(UfmsFeedbackDao dao) {
        this.dao = dao;
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return 插入到数据库后的实体
     */
    @NotNull
    @Override
    public UfmsFeedback insert(@NotNull UfmsFeedback entity) {
        return dao.save(entity);
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @NotNull
    @Override
    public Boolean delete(@NotNull Long id) {
        dao.deleteById(id);
        return dao.findById(id).isEmpty();
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return 更新后的实体
     */
    @NotNull
    @Override
    public UfmsFeedback update(@NotNull UfmsFeedback entity) {
        return dao.saveAndFlush(entity);
    }

    /**
     * 根据主键找到数据
     *
     * @param id 主键
     * @return 实体
     */
    @Nullable
    @Override
    public UfmsFeedback find(@NotNull Long id) {
        return dao.findById(id).orElse(null);
    }

    /**
     * 分页找到所有数据
     *
     * @param requestParams 参数
     * @return 分页的所有数据
     */
    @Override
    public Page<UfmsFeedback> findAll(Map<String, Object> requestParams) {
        return null;
    }

    /**
     * 分页找到所有数据
     *
     * @param specification specification
     * @param pageable      分页
     * @return 分页的所有数据
     */
    @Override
    public Page<UfmsFeedback> findAll(@NotNull Specification<UfmsFeedback> specification, @NotNull Pageable pageable) {
        return dao.findAll(specification, pageable);
    }


    /**
     * 根据关联的外键分页查询所有数据
     *
     * @param requestParams 参数
     * @return 分页的所有数据
     */
    @Override
    public Page<UfmsFeedback> findAllByForeignKey(@NotNull Map<String, Object> requestParams) {
        long productId = Long.parseLong(requestParams.get("productId").toString());
        int page = Integer.parseInt(requestParams.get("page").toString()) - 1;
        int limit = Integer.parseInt(requestParams.get("limit").toString());
        PageRequest pageRequest;
        if (requestParams.containsKey("type")) {
            String type = requestParams.get("type").toString();
            //最新方式排序
            if ("newest".equals(type)) {
                pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
            } else if ("recommend".equals(type)) {
                pageRequest = PageRequest.of(page, limit, Sort.by("isTopping", "isRecommend", "id").descending());
            } else {
                pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
            }
        } else {
            pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        }
        if (requestParams.containsKey("searchParams")) {
            JSONObject searchParams = JSON.parseObject(requestParams.get("searchParams").toString());
            String userUuid = "%" + searchParams.getOrDefault("userUuid", "") + "%";
            String userName = "%" + searchParams.getOrDefault("userName", "") + "%";
            String content = "%" + searchParams.getOrDefault("content", "") + "%";
            return dao.findAll((Specification<UfmsFeedback>) (root, query, criteriaBuilder) -> criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("product"), productId),
                    criteriaBuilder.like(root.get("content"), content),
                    criteriaBuilder.like(root.get("user").get("username"), userName),
                    criteriaBuilder.like(root.get("user").get("uuid"), userUuid)
            ), pageRequest);
        }
        return dao.findAll((Specification<UfmsFeedback>) (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("product"), productId)
        ), pageRequest);
    }

    @Override
    public Page<UfmsFeedback> findAllFeedbacks(@NotNull String type, @NotNull Integer page, @NotNull Integer limit, @NotNull Long productId) {
        PageRequest pageRequest;
        //layui默认从1开始，jpa默认从0开始
        page--;
        //最新方式排序
        if ("newest".equals(type)) {
            pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        } else if ("recommend".equals(type)) {
            pageRequest = PageRequest.of(page, limit, Sort.by("isTopping", "isRecommend", "likeCount", "id").descending());
        } else {
            pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        }
        Page<UfmsFeedback> originalFeedbackPage = dao.findAll((Specification<UfmsFeedback>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.<Boolean>get("isHidden"), false),
                root.get("original").isNull(),
                criteriaBuilder.equal(root.join("product"), productId)
        ), pageRequest);
        //查询主贴的回复
        originalFeedbackPage.getContent().forEach(feedback -> {
            Page<UfmsFeedback> replyPage = findAllFeedbackReplies(1, 3, feedback.getId(), feedback.getProduct().getId());
            if (!replyPage.isEmpty()) {
                feedback.setReplies(replyPage);
            }
        });
        return originalFeedbackPage;
    }

    /**
     * 查询主贴下所有回复
     *
     * @param page       页码，从1开始
     * @param limit      每页的大小
     * @param feedbackId 主贴id
     * @param productId  产品id
     * @return 分页的所有数据
     */
    @Override
    public Page<UfmsFeedback> findAllFeedbackReplies(@NotNull Integer page, @NotNull Integer limit, @NotNull Long feedbackId, @NotNull Long productId) {
        //点赞最多或者最新的在上面
        Sort sort = Sort.by("likeCount", "id").descending();
        //layui默认从1开始，jpa默认从0开始
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        return dao.findAll((Specification<UfmsFeedback>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.<Boolean>get("isHidden"), false),
                criteriaBuilder.equal(root.join("original"), feedbackId),
                criteriaBuilder.equal(root.join("product"), productId)
        ), pageRequest);
    }

    /**
     * 查询主贴下所有回复
     *
     * @param offset     偏移量，反馈详情的时候会用，新增评论会改变page和limit，
     * @param feedbackId 主贴id
     * @param productId  产品id
     * @return 分页的所有数据
     */
    @Override
    public Page<UfmsFeedback> findAllFeedbackReplies(@NotNull Integer offset, @NotNull Long feedbackId, @NotNull Long productId) {
        //点赞最多或最新的在上面
        Sort sort = Sort.by("likeCount", "id").descending();
        Pageable pageable = OffsetBasedPageRequest.of(offset, 10, sort);
        return dao.findAll((Specification<UfmsFeedback>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.<Boolean>get("isHidden"), false),
                criteriaBuilder.equal(root.join("original"), feedbackId),
                criteriaBuilder.equal(root.join("product"), productId)
        ), pageable);
    }

    /**
     * 查询用户所有反馈和回复
     *
     * @param page      页码，从1开始
     * @param limit     每页的大小
     * @param productId 产品id
     * @param userUuid  用户id
     * @return 分页的所有数据
     */
    @Override
    public Page<UfmsFeedback> findAllUserFeedbackAndReply(@NotNull Integer page, @NotNull Integer limit, @NotNull Long productId, @NotNull String userUuid) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("id").descending());
        return dao.findAll((Specification<UfmsFeedback>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("isHidden"), false),
                criteriaBuilder.equal(root.get("user"), new SysUser(userUuid)),
                criteriaBuilder.equal(root.get("product"), productId)
        ), pageRequest);
    }

    /**
     * 点赞某个反馈，likeCount+1
     *
     * @param feedbackId 反馈id
     * @return 是否成功
     */
    @Override
    public Boolean like(@NotNull Long feedbackId) {
        Optional<UfmsFeedback> byId = dao.findById(feedbackId);
        if (byId.isPresent()) {
            UfmsFeedback ufmsFeedback = byId.get();
            ufmsFeedback.setLikeCount(ufmsFeedback.getLikeCount() + 1);
            dao.saveAndFlush(ufmsFeedback);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 切换某个反馈的否标
     *
     * @param userId     用户id，用于权限校验
     * @param property   哪一个属性
     * @param feedbackId 反馈主贴id，original必须为null
     * @return 是否置顶
     */
    @Override
    public JsonResult<?> toggleIsProperty(@NotNull Long userId, @NotNull String property, @NotNull Long feedbackId) {
        Optional<UfmsFeedback> byId = dao.findById(feedbackId);
        Boolean result = null;
        if (byId.isPresent()) {
            UfmsFeedback ufmsFeedback = byId.get();
            if (ufmsFeedback.getProduct().getUser().getId().equals(userId)) {
                switch (property) {
                    case "top":
                        if (ufmsFeedback.getOriginal() == null) {
                            ufmsFeedback.setIsTopping(!ufmsFeedback.getIsTopping());
                            result = ufmsFeedback.getIsTopping();
                        } else {
                            return ResultTool.fail(ResultCode.ONLY_MAIN_FEEDBACK_CAN_BE_TOPPED);
                        }
                        break;
                    case "hide":
                        ufmsFeedback.setIsHidden(!ufmsFeedback.getIsHidden());
                        result = ufmsFeedback.getIsHidden();
                        break;
                    case "lock":
                        if (ufmsFeedback.getOriginal() == null) {
                            ufmsFeedback.setIsLocked(!ufmsFeedback.getIsLocked());
                            result = ufmsFeedback.getIsLocked();
                        } else {
                            return ResultTool.fail(ResultCode.ONLY_MAIN_FEEDBACK_CAN_BE_LOCKER);
                        }
                        break;
                    case "recommend":
                        if (ufmsFeedback.getOriginal() == null) {
                            ufmsFeedback.setIsRecommend(!ufmsFeedback.getIsRecommend());
                            result = ufmsFeedback.getIsRecommend();
                        } else {
                            return ResultTool.fail(ResultCode.ONLY_MAIN_FEEDBACK_CAN_BE_RECOMMENDED);
                        }
                        break;
                    default:
                        break;
                }
                if (result == null) {
                    return ResultTool.fail(ResultCode.PARAM_NOT_VALID);
                } else {
                    dao.saveAndFlush(ufmsFeedback);
                    return ResultTool.success(result);
                }
            } else {
                return ResultTool.fail(ResultCode.NO_PERMISSION);
            }
        } else {
            return ResultTool.fail(ResultCode.DATA_NOT_EXIST);
        }
    }

    /**
     * 根据起始时间查询新反馈个数
     *
     * @param productId 产品id
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @return 新反馈个数
     */
    @Override
    public Long countNewFeedbackByDate(@NotNull Long productId, @NotNull Date startDate, @NotNull Date endDate) {
        return dao.countDistinctByProductAndCreatedDateBetweenAndOriginal(new UfmsProduct(productId), startDate, endDate, null);
    }

    /**
     * 根据起始时间查询新回复个数
     *
     * @param productId    产品id
     * @param dayStartTime 起始时间
     * @param dayEndTime   结束时间
     * @return 新回复个数
     */
    @Override
    public Long countNewFeedbackReplyByDate(@NotNull Long productId, @NotNull Date dayStartTime, @NotNull Date dayEndTime) {
        return dao.count((Specification<UfmsFeedback>) (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("product"), new UfmsProduct(productId)),
                criteriaBuilder.between(root.get("createdDate"), dayStartTime, dayEndTime),
                root.get("original").isNotNull()
        ));
    }

    /**
     * 根据起始时间查询好问题个数
     *
     * @param productId    产品id
     * @param dayStartTime 起始时间
     * @param dayEndTime   结束时间
     * @return 好问题个数
     */
    @Override
    public Long countNewRecommendFeedbackByDate(@NotNull Long productId, @NotNull Date dayStartTime, @NotNull Date dayEndTime) {
        return dao.count((Specification<UfmsFeedback>) (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("product"), new UfmsProduct(productId)),
                criteriaBuilder.between(root.get("createdDate"), dayStartTime, dayEndTime),
                criteriaBuilder.equal(root.get("isRecommend"), true),
                root.get("original").isNull()
        ));
    }

    /**
     * 根据起始时间查询官方回复个数
     *
     * @param productId    产品id
     * @param dayStartTime 起始时间
     * @param dayEndTime   结束时间
     * @return 官方回复个数
     */
    @Override
    public Long countNewAdminFeedbackReplyByDate(@NotNull Long productId, @NotNull Date dayStartTime, @NotNull Date dayEndTime) {
        return dao.count((Specification<UfmsFeedback>) (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("product"), new UfmsProduct(productId)),
                criteriaBuilder.between(root.get("createdDate"), dayStartTime, dayEndTime),
                criteriaBuilder.equal(root.get("isAdmin"), true),
                root.get("original").isNotNull()
        ));
    }

    /**
     * 查询七天发帖回帖趋势
     *
     * @param productId 产品id
     * @return 七天发帖回帖趋势
     */
    @Override
    public Map<String, Object> querySevenDaysFeedbackAndAdminReply(@NotNull Long productId) {
        /**
         * xAxis: [
         *                 {
         *                     type: 'category',
         *                     boundaryGap: false,
         *                     data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
         *                 }
         *             ],
         *             yAxis: [
         *                 {
         *                     type: 'value'
         *                 }
         *             ],
         *             series: [
         *                 {
         *                     name: '邮件营销',
         *                     type: 'line',
         *                     stack: '总量',
         *                     areaStyle: {},
         *                     data: [120, 132, 101, 134, 90, 230, 210]
         *                 },
         *                 {
         *                     name: '联盟广告',
         *                     type: 'line',
         *                     areaStyle: {},
         *                     data: [220, 182, 191, 234, 290, 330, 310]
         *                 },
         *                 {
         *                     name: '视频广告',
         *                     type: 'line',
         *                     stack: '总量',
         *                     areaStyle: {},
         *                     data: [150, 232, 201, 154, 190, 330, 410]
         *                 },
         *                 {
         *                     name: '直接访问',
         *                     type: 'line',
         *                     stack: '总量',
         *                     areaStyle: {},
         *                     data: [320, 332, 301, 334, 390, 330, 320]
         *                 },
         *                 {
         *                     name: '搜索引擎',
         *                     type: 'line',
         *                     stack: '总量',
         *                     label: {
         *                         normal: {
         *                             show: true,
         *                             position: 'top'
         *                         }
         *                     },
         *                     areaStyle: {},
         *                     data: [820, 932, 901, 934, 1290, 1330, 1320]
         *                 }
         *             ]
         */
        Map<String, Object> map = new HashMap<>();
        List<String> xAxis = new ArrayList<>();
        List<Long> dayFeedbackList = new ArrayList<>();
        List<Long> dayAdminFeedbackReplyList = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            Date dayStartTime = DateUtil.getDayStartTime(-i);
            Date dayEndTime = DateUtil.getDayEndTime(-i);
            xAxis.add(new SimpleDateFormat("MM月dd日").format(dayStartTime));
            dayFeedbackList.add(countNewFeedbackByDate(productId, dayStartTime, dayEndTime));
            dayAdminFeedbackReplyList.add(countNewFeedbackReplyByDate(productId, dayStartTime, dayEndTime));
        }

        map.put("xAxis", xAxis);
        map.put("dayFeedbackList", dayFeedbackList);
        map.put("dayAdminFeedbackReply", dayAdminFeedbackReplyList);
        return map;
    }

    /**
     * 查询发帖内容词频
     *
     * @param productId 产品id
     * @param isDetail  是否不限制排名前几
     * @return 发帖内容词频
     */
    @Override
    public Map<String, Object> queryFeedbackWordFrequency(@NotNull Long productId, @NotNull Boolean isDetail) {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();
        dao.findAll((Specification<UfmsFeedback>) (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("product"), new UfmsProduct(productId)),
                root.get("original").isNull()
        )).forEach(feedback -> JcsegUtil.segmentString(feedback.getContent())
                .forEach((charSequence, integer) -> {
                    if (wordFrequencyMap.containsKey(charSequence)) {
                        wordFrequencyMap.put(charSequence, wordFrequencyMap.get(charSequence) + integer);
                    } else {
                        wordFrequencyMap.put(charSequence, integer);
                    }
                }));
        //降序排序
        HashMap<String, Integer> sortedWordMap = new LinkedHashMap<>();
        wordFrequencyMap.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue() - o1.getValue())
                .collect(Collectors.toList())
                .forEach(stringIntegerEntry -> {
                    sortedWordMap.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                });
        Map<String, Object> map = new HashMap<>();

        List<JSONObject> data = new ArrayList<>();

        int max = Math.min(7, sortedWordMap.size());
        sortedWordMap.forEach((s, integer) -> {
            //只查询排名前几的单词
            if (isDetail || data.size() != max) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", s);
                jsonObject.put("value", integer);
                data.add(jsonObject);
            }
        });
        map.put("data", data);
        return map;
    }


    static class MapValueComparator implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> me1, Map.Entry<String, Integer> me2) {
            return me2.getValue().compareTo(me1.getValue());
        }
    }

    /**
     * 使用 Map按value进行排序
     *
     * @param oriMap 原map
     * @return 最后一个元素是[total=xxx]
     */
    public static Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(oriMap.entrySet());
        entryList.sort(new MapValueComparator());
        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
        Map.Entry<String, Integer> tmpEntry;
        int total = 0;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            Integer value = tmpEntry.getValue();
            total += value;
            sortedMap.put(tmpEntry.getKey(), value);
        }
        sortedMap.put("total", total);
        return sortedMap;
    }

}
