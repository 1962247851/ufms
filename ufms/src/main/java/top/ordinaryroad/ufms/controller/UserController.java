package top.ordinaryroad.ufms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.Constant;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import top.ordinaryroad.ufms.entity.SysUser;
import top.ordinaryroad.ufms.redis.RedisCache;
import top.ordinaryroad.ufms.service.SysUserService;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 用户控制器
 *
 * @author qq1962247851
 * @date 2020/11/14 12:12
 **/
@Api
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseUserController {

    private final SysUserService userService;
    private final RedisCache redisCache;

    public UserController(SysUserService userService, RedisCache redisCache, HttpSession session) {
        this.userService = userService;
        this.redisCache = redisCache;
        this.session = session;
    }

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public JsonResult<?> login(
            @RequestParam String name, @RequestParam String password,
            @RequestParam(required = false) String uuid, @RequestParam(required = false) String code
    ) {
        if (code != null && uuid != null) {
            // 下面是验证逻辑，验证通过则返回UsernamePasswordAuthenticationToken，
            String verifyKey = Constant.CAPTCHA_CODE_KEY + uuid;
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                return ResultTool.fail(ResultCode.CODE_NOT_EXIST);
            }
            if (!code.equalsIgnoreCase(captcha)) {
                return ResultTool.fail(ResultCode.CODE_WRONG);
            }
        } else {
            return ResultTool.fail(ResultCode.PARAM_NOT_COMPLETE);
        }
        SysUser sysUser = userService.selectByUserName(name);
        if (sysUser == null) {
            return ResultTool.fail(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }
        if (!sysUser.getPassword().equals(encodePassword(password))) {
            return ResultTool.fail(ResultCode.USER_CREDENTIALS_ERROR);
        }
        //登录成功
        sysUser.setLastLoginTime(new Date());
        SysUser update = userService.update(sysUser);
        session.setAttribute("user", update);
        return ResultTool.success(update);
    }

    @ApiOperation(value = "退出登录")
    @GetMapping("logout")
    public JsonResult<?> logout() {
        session.removeAttribute("user");
        return ResultTool.success();
    }


    public static void main(String[] args) {
        String s = encodePassword("admin");
        System.out.println(s);
    }

    @Nullable
    private static String encodePassword(@NotNull String password) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("SHA");
            md5.update(password.getBytes());
            return new BigInteger(md5.digest()).toString(32);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
