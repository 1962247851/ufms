package top.ordinaryroad.ufms.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import top.ordinaryroad.ufms.config.security.service.UserDetailsServiceImpl;
import top.ordinaryroad.ufms.entity.SysUser;
import top.ordinaryroad.ufms.exception.UserNotLoginException;

/**
 * 从Session中获取User控制器
 *
 * @author qq1962247851
 * @date 2020/11/14 16:31
 **/
public class BaseUserController {

    public SysUser getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        if (principal == "anonymousUser") {
            throw new UserNotLoginException("用户未登录");
        }
        UserDetailsServiceImpl.MyUserDetails userDetails = (UserDetailsServiceImpl.MyUserDetails) principal;
        return userDetails.getSysUser();
    }
}
