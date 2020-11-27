package top.ordinaryroad.ufms.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.ordinaryroad.ufms.entity.UfmsFeedback;
import top.ordinaryroad.ufms.entity.UfmsProduct;

/**
 * 用户反馈数据库操作类
 *
 * @author qq1962247851
 * @date 2020/11/17 9:52
 **/
public interface UfmsFeedbackDao extends JpaRepository<UfmsFeedback, Long>, JpaSpecificationExecutor<UfmsFeedback> {

    /**
     * 根据产品id分页查询反馈
     *
     * @param product  产品
     * @param isHidden 是否隐藏
     * @param pageable 分页
     * @return 反馈列表
     */
    Page<UfmsFeedback> findAllByProductAndIsHidden(UfmsProduct product, Boolean isHidden, Pageable pageable);

    /**
     * 根据用户名、反馈内容、用户唯一标识符查询反馈
     *
     * @param product  产品
     * @param userName 用户名
     * @param content  反馈内容
     * @param userUuid 用户唯一标识符
     * @param isHidden 是否隐藏
     * @param pageable 分页
     * @return 反馈列表
     */
    Page<UfmsFeedback> findAllByProductAndUserNameLikeAndContentLikeAndUserUuidLikeAndIsHidden(UfmsProduct product, String userName, String content, String userUuid, Boolean isHidden, Pageable pageable);

    /**
     * 根据用户唯一标识符查询反馈
     *
     * @param product  产品
     * @param userUuid 用户唯一标识符
     * @param isHidden 是否隐藏
     * @param pageable 分页
     * @return 反馈列表
     */
    Page<UfmsFeedback> findAllByProductAndUserUuidLikeAndIsHidden(UfmsProduct product, String userUuid, Boolean isHidden, Pageable pageable);

    /**
     * 根据用户名查询反馈
     *
     * @param product  产品
     * @param userName 用户名
     * @param isHidden 是否隐藏
     * @param pageable 分页
     * @return 反馈列表
     */
    Page<UfmsFeedback> findAllByProductAndUserNameLikeAndIsHidden(UfmsProduct product, String userName, Boolean isHidden, Pageable pageable);

    /**
     * 根据反馈内容查询反馈
     *
     * @param product  产品
     * @param content  反馈内容
     * @param isHidden 是否隐藏
     * @param pageable 分页
     * @return 反馈列表
     */
    Page<UfmsFeedback> findAllByProductAndContentLikeAndIsHidden(UfmsProduct product, String content, Boolean isHidden, Pageable pageable);


}
