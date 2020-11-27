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
import top.ordinaryroad.ufms.common.utils.OffsetBasedPageRequest;
import top.ordinaryroad.ufms.dao.UfmsFeedbackDao;
import top.ordinaryroad.ufms.entity.UfmsFeedback;
import top.ordinaryroad.ufms.entity.UfmsProduct;
import top.ordinaryroad.ufms.service.UfmsFeedbackService;

import java.util.Map;

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
            String userUuid = "";
            String userName = "";
            String content = "";
            if (searchParams.containsKey("userUuid") && searchParams.containsKey("userName") && searchParams.containsKey("content")) {
                userUuid = "%" + searchParams.get("userUuid") + "%";
                userName = "%" + searchParams.get("userName") + "%";
                content = "%" + searchParams.get("content") + "%";
                return dao.findAllByProductAndUserNameLikeAndContentLikeAndUserUuidLikeAndIsHidden(new UfmsProduct(productId), userName, content, userUuid, false, pageRequest);
            } else if (searchParams.containsKey("userUuid")) {
                return dao.findAllByProductAndUserUuidLikeAndIsHidden(new UfmsProduct(productId), userUuid, false, pageRequest);
            } else if (searchParams.containsKey("userName")) {
                return dao.findAllByProductAndUserNameLikeAndIsHidden(new UfmsProduct(productId), userName, false, pageRequest);
            } else if (searchParams.containsKey("content")) {
                return dao.findAllByProductAndContentLikeAndIsHidden(new UfmsProduct(productId), content, false, pageRequest);
            }
//            else {
//                return dao.findAllByProductAndIsHidden(new UfmsProduct(productId), false, pageRequest);
//            }
        }
        return dao.findAllByProductAndIsHidden(new UfmsProduct(productId), false, pageRequest);
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
            pageRequest = PageRequest.of(page, limit, Sort.by("isTopping", "isRecommend", "id").descending());
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
        //layui默认从1开始，jpa默认从0开始
        //最新的在上面
        Sort sort;
//        if (limit == 3) {
        sort = Sort.by("id").descending();
//        } else {
//            sort = Sort.by("id").ascending();
//        }
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
        //layui默认从1开始，jpa默认从0开始
        //最新的在上面
        Sort sort;
//        if (limit == 3) {
        sort = Sort.by("id").descending();
//        } else {
//            sort = Sort.by("id").ascending();
//        }
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
     * @param userId    用户id
     * @return 分页的所有数据
     */
    @Override
    public Page<UfmsFeedback> findAllUserFeedbackAndReply(@NotNull Integer page, @NotNull Integer limit, @NotNull Long productId, @NotNull String userId) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("id").descending());
        return dao.findAll((Specification<UfmsFeedback>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.<Boolean>get("isHidden"), false),
                criteriaBuilder.equal(root.<String>get("userUuid"), userId),
                criteriaBuilder.equal(root.join("product"), productId)
        ), pageRequest);
    }

}
