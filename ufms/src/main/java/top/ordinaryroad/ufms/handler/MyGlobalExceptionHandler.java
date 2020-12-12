package top.ordinaryroad.ufms.handler;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import top.ordinaryroad.ufms.exception.UserNotLoginException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 19622
 */
@ControllerAdvice
@ResponseBody
public class MyGlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public JsonResult<?> exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
//        throw e;
        e.printStackTrace();

        //如果包含状态码就自动按照状态码展示对应的界面
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //记录异常，打印到控制台
        logger.error(
                "\nRequest URL : {}\nException : {}\nRequest User : {}\n",
                request.getRequestURL(), e, JSON.toJSONString(principal)
        );
        //记录到文件里
        logger.error(
                "\nRequest URL : {}\nException : {}\nRequest User : {}\n",
                request.getRequestURL(), e.getMessage(), JSON.toJSONString(principal)
        );
        if (e instanceof UserNotLoginException) {
            return ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        }
        return ResultTool.fail();
    }
}
