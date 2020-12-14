package top.ordinaryroad.ufms.controller;

import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import kohgylw.kcnamer.core.KCNamer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import top.ordinaryroad.ufms.common.controller.ForeignKeyAbstractCrudController;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.IdUtils;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import top.ordinaryroad.ufms.entity.SysUser;
import top.ordinaryroad.ufms.entity.SysUserRoleRelation;
import top.ordinaryroad.ufms.entity.UfmsFeedback;
import top.ordinaryroad.ufms.exception.UserNotLoginException;
import top.ordinaryroad.ufms.service.SysUserRoleRelationService;
import top.ordinaryroad.ufms.service.SysUserService;
import top.ordinaryroad.ufms.service.UfmsFeedbackService;
import top.ordinaryroad.ufms.service.UfmsProductService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户反馈控制器
 *
 * @author qq1962247851
 * @date 2020/11/17 9:56
 **/
@Api
@RestController
@RequestMapping(value = "/api/feedback", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FeedbackController extends BaseUserController implements ForeignKeyAbstractCrudController<UfmsFeedback, Long> {

    private final UfmsFeedbackService feedbackService;
    private final UfmsProductService productService;
    private final SysUserService userService;
    private final SysUserRoleRelationService userRoleRelationService;

    public FeedbackController(UfmsFeedbackService feedbackService, UfmsProductService productService, SysUserService userService, SysUserRoleRelationService userRoleRelationService) {
        this.feedbackService = feedbackService;
        this.productService = productService;
        this.userService = userService;
        this.userRoleRelationService = userRoleRelationService;
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return JsonString
     */
    @Override
    public JsonResult<?> insert(@RequestBody UfmsFeedback entity) {
        JsonResult<?> jsonResult = entity.checkValid(true);
        if (jsonResult != null) {
            return jsonResult;
        }
        if (StringUtil.isNullOrEmpty(entity.getUuid())) {
            entity.setUuid(IdUtils.fastSimpleUUID());
        }
        //主贴判断
        UfmsFeedback original = entity.getOriginal();
        if (original != null) {
            UfmsFeedback originalFind = feedbackService.find(original.getId());
            if (originalFind == null) {
                return ResultTool.fail(ResultCode.ORIGINAL_FEEDBACK_NOT_EXIST);
            } else {
                if (originalFind.getIsLocked()) {
                    return ResultTool.fail(ResultCode.ORIGINAL_FEEDBACK_LOCKED);
                }
            }
        }
        entity.setProduct(productService.find(entity.getProduct().getId()));
        //获取已登录的user
        SysUser user;
        try {
            user = getUser();
        } catch (UserNotLoginException e) {
            user = null;
        }
        if (entity.getProduct().getUser().equals(user)) {
            entity.setIsAdmin(true);
            entity.setUser(userService.selectByUuid(user.getUuid()));
        } else {
            entity.setIsAdmin(false);
            SysUser entityUser = entity.getUser();
            if (entityUser == null || StringUtil.isNullOrEmpty(entityUser.getUuid()) || StringUtil.isNullOrEmpty(entityUser.getUsername())) {
                entity.setUser(new SysUser(IdUtils.fastSimpleUUID(), new KCNamer().getRandomName(), null));
            } else {
                //查询user表中是否存在自定义的user
                SysUser customUser = userService.selectByUuid(entityUser.getUuid());
                if (customUser == null) {
                    //保存自定义user
                    SysUser insert = userService.insert(entityUser);
                    userRoleRelationService.insert(new SysUserRoleRelation(insert.getId(), 2L));
                } else {
                    if (!Objects.equals(customUser.getAvatar(), entityUser.getAvatar())) {
                        customUser.setAvatar(entityUser.getAvatar());
                        customUser = userService.update(customUser);
                    }
                }
                entity.setUser(customUser);
            }
        }
        return ResultTool.success(feedbackService.insert(entity));
    }

    /**
     * 删除
     *
     * @param ids 主键列表
     * @return JsonString
     */
    @Override
    public JsonResult<?> delete(@RequestBody List<Long> ids) {
        ids.forEach(id -> {
            UfmsFeedback find = feedbackService.find(id);
            if (find != null && find.getProduct().getUser().equals(getUser())) {
                feedbackService.delete(id);
            }
        });
        return ResultTool.success();
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return JsonString
     */
    @Override
    public JsonResult<?> update(@RequestBody UfmsFeedback entity) {
        JsonResult<?> jsonResult = entity.checkValid();
        if (jsonResult != null) {
            return jsonResult;
        }
        return ResultTool.success(feedbackService.update(entity));
    }

    /**
     * 根据主键找到数据
     *
     * @param id 主键
     * @return JsonString
     */
    @Override
    public JsonResult<?> find(@RequestParam Long id) {
        UfmsFeedback ufmsFeedback = feedbackService.find(id);
        if (ufmsFeedback == null) {
            return ResultTool.fail(ResultCode.DATA_NOT_EXIST);
        } else {
            return ResultTool.success(ufmsFeedback);
        }
    }

    /**
     * 分页找到所有数据
     *
     * @param requestParams 参数
     * @return JsonString
     */
    @Override
    public JsonResult<?> findAll(@RequestParam Map<String, Object> requestParams) {
        return null;
    }

    /**
     * 根据关联的外键分页找到所有数据
     *
     * @param requestParams 请求参数
     * @return 分页的所有数据
     */
    @Override
    public JsonResult<?> findAllByForeignKey(@RequestParam Map<String, Object> requestParams) {
        if (!requestParams.containsKey("limit")) {
            requestParams.put("limit", 10);
        }
        return ResultTool.success(feedbackService.findAllByForeignKey(requestParams));
    }

    /**
     * 查询所有反馈（主贴）
     *
     * @param type      类型[recommend,newest]
     * @param page      页码，从1开始
     * @param limit     每页的大小
     * @param productId 产品id
     * @return 分页的所有数据，包含最新的3条回复
     */
    @GetMapping("findAllFeedbacks")
    public JsonResult<?> findAllFeedbacks(
            @RequestParam(defaultValue = "recommend") String type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) Long productId
    ) {
        if (productId == null) {
            return ResultTool.fail(ResultCode.PRODUCT_ID_NOT_NULL);
        }
        return ResultTool.success(feedbackService.findAllFeedbacks(type, page, limit, productId));
    }

    @GetMapping("findAllFeedbackReplies")
    public JsonResult<?> findAllFeedbackReplies(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Long feedbackId,
            @RequestParam(required = false) Long productId
    ) {
        if (productId == null) {
            return ResultTool.fail(ResultCode.PRODUCT_ID_NOT_NULL);
        }
        if (feedbackId == null) {
            return ResultTool.fail(ResultCode.FEEDBACK_ID_NOT_NULL);
        }
        if (offset == null) {
            return ResultTool.success(feedbackService.findAllFeedbackReplies(page, limit, feedbackId, productId));
        } else {
            return ResultTool.success(feedbackService.findAllFeedbackReplies(offset, feedbackId, productId));
        }
    }

    @GetMapping("findAllUserFeedbackAndReply")
    public JsonResult<?> findAllUserFeedbackAndReply(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String userUuid
    ) {
        if (productId == null) {
            return ResultTool.fail(ResultCode.PRODUCT_ID_NOT_NULL);
        }
        if (userUuid == null) {
            return ResultTool.fail(ResultCode.USER_ID_NOT_NULL);
        }
        return ResultTool.success(feedbackService.findAllUserFeedbackAndReply(page, limit, productId, userUuid));
    }

    @PostMapping("like")
    public JsonResult<?> like(@RequestParam Long feedbackId) {
        return new JsonResult<>(feedbackService.like(feedbackId), ResultCode.DATA_NOT_EXIST);
    }

    @PostMapping("toggleIsProperty")
    public JsonResult<?> toggleIsProperty(
            @RequestParam(required = false) String property,
            @RequestParam(required = false) Long feedbackId
    ) {
        if (property == null || feedbackId == null) {
            return ResultTool.fail(ResultCode.PARAM_NOT_COMPLETE);
        } else {
            return feedbackService.toggleIsProperty(getUser().getId(), property, feedbackId);
        }
    }

}
