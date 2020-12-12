package top.ordinaryroad.ufms.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.DateUtil;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import top.ordinaryroad.ufms.entity.UfmsProduct;
import top.ordinaryroad.ufms.service.UfmsFeedbackService;
import top.ordinaryroad.ufms.service.UfmsProductService;

/**
 * 统计控制器
 *
 * @author qq1962247851
 * @date 2020/12/4 20:54
 **/
@Api
@RestController
@RequestMapping(value = "/api/statistic", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StatisticController extends BaseUserController {

    private final UfmsFeedbackService feedbackService;
    private final UfmsProductService productService;

    public StatisticController(UfmsFeedbackService feedbackService, UfmsProductService productService) {
        this.feedbackService = feedbackService;
        this.productService = productService;
    }

    /**
     * 查询新的反馈个数
     *
     * @param productId 产品id
     * @return jsonResult
     */
    @GetMapping("queryFeedbackCount/{productId}")
    public JsonResult<?> queryFeedbackCount(@PathVariable Long productId) {
        JsonResult<?> validResult = checkValid(productId);
        if (validResult != null) {
            return validResult;
        }

        Long todayCount = feedbackService.countNewFeedbackByDate(productId, DateUtil.getDayStartTime(0), DateUtil.getDayEndTime(0));
        Long yesterdayCount = feedbackService.countNewFeedbackByDate(productId, DateUtil.getDayStartTime(-1), DateUtil.getDayEndTime(-1));
        Long sevenDaysCount = feedbackService.countNewFeedbackByDate(productId, DateUtil.getDayStartTime(-7), DateUtil.getDayEndTime(-1));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("todayCount", todayCount);
        jsonObject.put("yesterday", todayCount - yesterdayCount);
        jsonObject.put("sevenDaysCount", sevenDaysCount);
        return ResultTool.success(jsonObject);
    }

    /**
     * 查询回复个数
     *
     * @param productId 产品id
     * @return jsonResult
     */
    @GetMapping("queryFeedbackReplyCount/{productId}")
    public JsonResult<?> queryFeedbackReplyCount(@PathVariable Long productId) {
        JsonResult<?> validResult = checkValid(productId);
        if (validResult != null) {
            return validResult;
        }
        Long todayCount = feedbackService.countNewFeedbackReplyByDate(productId, DateUtil.getDayStartTime(0), DateUtil.getDayEndTime(0));
        Long yesterdayCount = feedbackService.countNewFeedbackReplyByDate(productId, DateUtil.getDayStartTime(-1), DateUtil.getDayEndTime(-1));
        Long sevenDaysCount = feedbackService.countNewFeedbackReplyByDate(productId, DateUtil.getDayStartTime(-7), DateUtil.getDayEndTime(-1));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("todayCount", todayCount);
        jsonObject.put("yesterday", todayCount - yesterdayCount);
        jsonObject.put("sevenDaysCount", sevenDaysCount);
        return ResultTool.success(jsonObject);
    }

    /**
     * 查询好问题个数
     *
     * @param productId 产品id
     * @return jsonResult
     */
    @GetMapping("queryRecommendFeedbackCount/{productId}")
    public JsonResult<?> queryRecommendFeedbackCount(@PathVariable Long productId) {
        JsonResult<?> validResult = checkValid(productId);
        if (validResult != null) {
            return validResult;
        }
        Long todayCount = feedbackService.countNewRecommendFeedbackByDate(productId, DateUtil.getDayStartTime(0), DateUtil.getDayEndTime(0));
        Long yesterdayCount = feedbackService.countNewRecommendFeedbackByDate(productId, DateUtil.getDayStartTime(-1), DateUtil.getDayEndTime(-1));
        Long sevenDaysCount = feedbackService.countNewRecommendFeedbackByDate(productId, DateUtil.getDayStartTime(-7), DateUtil.getDayEndTime(-1));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("todayCount", todayCount);
        jsonObject.put("yesterday", todayCount - yesterdayCount);
        jsonObject.put("sevenDaysCount", sevenDaysCount);
        return ResultTool.success(jsonObject);
    }

    /**
     * 查询官方回复个数
     *
     * @param productId 产品id
     * @return jsonResult
     */
    @GetMapping("queryAdminFeedbackReplyCount/{productId}")
    public JsonResult<?> queryAdminFeedbackReplyCount(@PathVariable Long productId) {
        JsonResult<?> validResult = checkValid(productId);
        if (validResult != null) {
            return validResult;
        }
        Long todayCount = feedbackService.countNewAdminFeedbackReplyByDate(productId, DateUtil.getDayStartTime(0), DateUtil.getDayEndTime(0));
        Long yesterdayCount = feedbackService.countNewAdminFeedbackReplyByDate(productId, DateUtil.getDayStartTime(-1), DateUtil.getDayEndTime(-1));
        Long sevenDaysCount = feedbackService.countNewAdminFeedbackReplyByDate(productId, DateUtil.getDayStartTime(-7), DateUtil.getDayEndTime(-1));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("todayCount", todayCount);
        jsonObject.put("yesterday", todayCount - yesterdayCount);
        jsonObject.put("sevenDaysCount", sevenDaysCount);
        return ResultTool.success(jsonObject);
    }

    /**
     * 查询七天发帖回帖趋势
     *
     * @param productId 产品id
     * @return jsonResult
     */
    @GetMapping("querySevenDaysFeedbackAndAdminReply/{productId}")
    public JsonResult<?> querySevenDaysFeedbackAndAdminReply(@PathVariable Long productId) {
        JsonResult<?> validResult = checkValid(productId);
        if (validResult != null) {
            return validResult;
        }
        return ResultTool.success(feedbackService.querySevenDaysFeedbackAndAdminReply(productId));
    }

    /**
     * 查询发帖内容词频
     *
     * @param productId 产品id
     * @return jsonResult
     */
    @GetMapping("queryFeedbackWordFrequency/{productId}/{isDetail}")
    public JsonResult<?> queryFeedbackWordFrequency(@PathVariable Long productId, @PathVariable Boolean isDetail) {
        JsonResult<?> validResult = checkValid(productId);
        if (validResult != null) {
            return validResult;
        }
        return ResultTool.success(feedbackService.queryFeedbackWordFrequency(productId, isDetail));
    }

    /**
     * 检查输入是否合法
     *
     * @param productId 产品id
     * @return validResult
     */
    @Nullable
    private JsonResult<?> checkValid(Long productId) {
        UfmsProduct ufmsProduct = productService.find(productId);
        if (ufmsProduct == null) {
            return ResultTool.fail(ResultCode.DATA_NOT_EXIST);
        }
        if (!ufmsProduct.getUser().equals(getUser())) {
            return ResultTool.fail(ResultCode.NO_PERMISSION);
        }
        return null;
    }

}
