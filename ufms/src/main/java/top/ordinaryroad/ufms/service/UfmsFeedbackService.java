package top.ordinaryroad.ufms.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.service.ForeignKeyCrudService;
import top.ordinaryroad.ufms.entity.UfmsFeedback;

import java.util.Date;
import java.util.Map;


/**
 * 用户反馈服务类
 *
 * @author qq1962247851
 * @date 2020/11/17 9:53
 **/
public interface UfmsFeedbackService extends ForeignKeyCrudService<UfmsFeedback, Long> {

    /**
     * 查询所有反馈（主贴）
     *
     * @param type      类型[recommend,newest]
     * @param page      页码，从1开始
     * @param limit     每页的大小
     * @param productId 产品id
     * @return 分页的所有数据
     */
    Page<UfmsFeedback> findAllFeedbacks(@NotNull String type, @NotNull Integer page, @NotNull Integer limit, @NotNull Long productId);

    /**
     * 查询主贴下所有回复
     *
     * @param page       页码，从1开始
     * @param limit      每页的大小
     * @param feedbackId 主贴id
     * @param productId  产品id
     * @return 分页的所有数据
     */
    Page<UfmsFeedback> findAllFeedbackReplies(@NotNull Integer page, @NotNull Integer limit, @NotNull Long feedbackId, @NotNull Long productId);

    /**
     * 查询主贴下所有回复
     *
     * @param offset     偏移量，反馈详情的时候会用，新增评论会改变page和limit，
     * @param feedbackId 主贴id
     * @param productId  产品id
     * @return 分页的所有数据
     */
    Page<UfmsFeedback> findAllFeedbackReplies(@NotNull Integer offset, @NotNull Long feedbackId, @NotNull Long productId);

    /**
     * 查询用户所有反馈和回复
     *
     * @param page      页码，从1开始
     * @param limit     每页的大小
     * @param productId 产品id
     * @param userId    用户id
     * @return 分页的所有数据
     */
    Page<UfmsFeedback> findAllUserFeedbackAndReply(@NotNull Integer page, @NotNull Integer limit, @NotNull Long productId, @NotNull String userId);

    /**
     * 点赞某个反馈，likeCount+1
     *
     * @param feedbackId 反馈id
     * @return 是否成功
     */
    Boolean like(@NotNull Long feedbackId);

    /**
     * 切换某个反馈的否标
     *
     * @param userId     用户id，用于权限校验
     * @param property   哪一个属性
     * @param feedbackId 反馈主贴id，original必须为null
     * @return 是否置顶
     */
    JsonResult<?> toggleIsProperty(@NotNull Long userId, @NotNull String property, @NotNull Long feedbackId);

    /**
     * 根据起始时间查询新反馈个数
     *
     * @param productId 产品id
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @return 新反馈个数
     */
    Long countNewFeedbackByDate(@NotNull Long productId, @NotNull Date startDate, @NotNull Date endDate);

    /**
     * 根据起始时间查询新回复个数
     *
     * @param productId    产品id
     * @param dayStartTime 起始时间
     * @param dayEndTime   结束时间
     * @return 新回复个数
     */
    Long countNewFeedbackReplyByDate(@NotNull Long productId, @NotNull Date dayStartTime, @NotNull Date dayEndTime);

    /**
     * 根据起始时间查询好问题个数
     *
     * @param productId    产品id
     * @param dayStartTime 起始时间
     * @param dayEndTime   结束时间
     * @return 好问题个数
     */
    Long countNewRecommendFeedbackByDate(@NotNull Long productId, @NotNull Date dayStartTime, @NotNull Date dayEndTime);

    /**
     * 根据起始时间查询官方回复个数
     *
     * @param productId    产品id
     * @param dayStartTime 起始时间
     * @param dayEndTime   结束时间
     * @return 官方回复个数
     */
    Long countNewAdminFeedbackReplyByDate(@NotNull Long productId, @NotNull Date dayStartTime, @NotNull Date dayEndTime);

    /**
     * 查询七天发帖回帖趋势
     *
     * @param productId 产品id
     * @return 七天发帖回帖趋势
     */
    Map<String, Object> querySevenDaysFeedbackAndAdminReply(@NotNull Long productId);

    /**
     * 查询发帖内容词频
     *
     * @param productId 产品id
     * @param isDetail  是否不限制排名前几
     * @return 发帖内容词频
     */
    Map<String, Object> queryFeedbackWordFrequency(@NotNull Long productId, @NotNull Boolean isDetail);
}
