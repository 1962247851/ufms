package top.ordinaryroad.ufms.controller;

import top.ordinaryroad.ufms.entity.SysUser;
import top.ordinaryroad.ufms.exception.UserNotLoginException;

import javax.servlet.http.HttpSession;

/**
 * 从Session中获取User控制器
 *
 * @author qq1962247851
 * @date 2020/11/14 16:31
 **/
public class BaseUserController {

    protected HttpSession session;

    public SysUser getUser() {
        Object user = session.getAttribute("user");
        if (user == null) {
            throw new UserNotLoginException("用户未登录");
        }
        return (SysUser) user;
    }
}
