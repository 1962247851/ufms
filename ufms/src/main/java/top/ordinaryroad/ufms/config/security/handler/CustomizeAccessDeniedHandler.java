package top.ordinaryroad.ufms.config.security.handler;

import com.alibaba.fastjson.JSONObject;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限拒绝处理逻辑
 */
@Component
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JsonResult<?> result = ResultTool.fail(ResultCode.NO_PERMISSION);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
    }
}
