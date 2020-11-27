package top.ordinaryroad.ufms.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.ResultTool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 19622
 */
@ControllerAdvice
@ResponseBody
public class MyGlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public JsonResult<?> exceptionHandler(HttpSession session, HttpServletRequest request, Exception e) throws Exception {
//        throw e;

        //如果包含状态码就自动按照状态码展示对应的界面
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        //记录异常，打印到控制台
//        logger.error("\nRequest URL : {}\nException : {}", request.getRequestURL(), e);

        Object user = session.getAttribute("user");
        //记录到文件里
        logger.error(
                "\nRequest URL : {}\nException : {}\nRequest User : {}\n",
                request.getRequestURL(), e.getMessage(), user
        );
        if (user == null) {
            return ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        }
        return ResultTool.fail();
    }
}
