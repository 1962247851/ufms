package top.ordinaryroad.ufms.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import top.ordinaryroad.ufms.common.service.ForeignKeyCrudService;
import top.ordinaryroad.ufms.entity.UfmsFeedback;


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
}
