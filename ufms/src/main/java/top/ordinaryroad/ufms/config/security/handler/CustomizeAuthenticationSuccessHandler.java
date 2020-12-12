package top.ordinaryroad.ufms.config.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import top.ordinaryroad.ufms.config.security.service.UserDetailsServiceImpl;
import top.ordinaryroad.ufms.entity.SysUser;
import top.ordinaryroad.ufms.service.SysUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

/**
 * 登录成功处理逻辑
 */
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final SysUserService sysUserService;

    public CustomizeAuthenticationSuccessHandler(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //更新用户表上次登录时间、更新人、更新时间等字段
        String username = ((UserDetailsServiceImpl.MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        SysUser sysUser = sysUserService.selectByUserName(username);
        sysUser.setLastLoginTime(Calendar.getInstance().getTime());
        SysUser update = sysUserService.update(sysUser);

        //TODO 此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展
        //返回json数据
        JsonResult<SysUser> result = ResultTool.success(update);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(result.toString());
    }
}
