package top.ordinaryroad.ufms.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import kohgylw.kcnamer.core.KCNamer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.Constant;
import top.ordinaryroad.ufms.common.utils.IdUtils;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import top.ordinaryroad.ufms.entity.SysUser;
import top.ordinaryroad.ufms.entity.SysUserRoleRelation;
import top.ordinaryroad.ufms.redis.RedisCache;
import top.ordinaryroad.ufms.service.MailService;
import top.ordinaryroad.ufms.service.SysUserRoleRelationService;
import top.ordinaryroad.ufms.service.SysUserService;

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
    private final MailService mailService;
    private final SysUserRoleRelationService userRoleRelationService;

    public UserController(SysUserService userService, RedisCache redisCache, MailService mailService, SysUserRoleRelationService userRoleRelationService) {
        this.userService = userService;
        this.redisCache = redisCache;
        this.mailService = mailService;
        this.userRoleRelationService = userRoleRelationService;
    }

//    @ApiOperation(value = "登录")
//    @PostMapping("login")
//    public JsonResult<?> login(
//            @RequestParam String name, @RequestParam String password,
//            @RequestParam(required = false) String uuid, @RequestParam(required = false) String code
//    ) {
//        if (code != null && uuid != null) {
//            // 下面是验证逻辑，验证通过则返回UsernamePasswordAuthenticationToken，
//            String verifyKey = Constant.CAPTCHA_CODE_KEY + uuid;
//            String captcha = redisCache.getCacheObject(verifyKey);
//            redisCache.deleteObject(verifyKey);
//            if (captcha == null) {
//                return ResultTool.fail(ResultCode.CODE_NOT_EXIST);
//            }
//            if (!code.equalsIgnoreCase(captcha)) {
//                return ResultTool.fail(ResultCode.CODE_WRONG);
//            }
//        } else {
//            return ResultTool.fail(ResultCode.PARAM_NOT_COMPLETE);
//        }
//        SysUser sysUser = userService.selectByUserName(name);
//        if (sysUser == null) {
//            return ResultTool.fail(ResultCode.USER_ACCOUNT_NOT_EXIST);
//        }
//        if (!sysUser.getPassword().equals(encodePassword(password))) {
//            return ResultTool.fail(ResultCode.USER_CREDENTIALS_ERROR);
//        }
//        //登录成功
//        sysUser.setLastLoginTime(new Date());
//        SysUser update = userService.update(sysUser);
//        session.setAttribute("user", update);
//        return ResultTool.success(update);
//    }
//
//    @ApiOperation(value = "退出登录")
//    @GetMapping("logout")
//    public JsonResult<?> logout() {
//        session.removeAttribute("user");
//        return ResultTool.success();
//    }

    @ApiOperation(value = "登录", notes = "第一次登录或者退出登录需要验证码，登录成功后则不需要")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 2003, message = "密码错误"),
            @ApiResponse(code = 2007, message = "账号不存在"),
            @ApiResponse(code = 4013, message = "验证码失效"),
            @ApiResponse(code = 4014, message = "验证码错误"),
            @ApiResponse(code = 1004, message = "参数缺失")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码"),
            @ApiImplicitParam(name = "uuid", value = "获取验证码图片后响应中的uuid，原封不动传过来就行"),
            @ApiImplicitParam(name = "code", value = "验证码")
    })
    @PostMapping("login")
    private JsonResult<?> login(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) String uuid, @RequestParam(required = false) String code) {
        return ResultTool.success(new SysUser());
    }

    @ApiOperation(value = "登出")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @GetMapping("logout")
    private JsonResult<?> logout() {
        return ResultTool.success();
    }

    @ApiOperation(value = "注册")
    @PostMapping("register")
    public JsonResult<?> register(@RequestBody SysUser entity, @RequestParam String uuid, @RequestParam String code) {
        String verifyKey = Constant.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        if (captcha == null) {
            return ResultTool.fail(ResultCode.CODE_NOT_EXIST);
        }
        if (!code.equalsIgnoreCase(captcha)) {
            return ResultTool.fail(ResultCode.CODE_WRONG);
        }
        if (entity.getId() != null) {
            entity.setId(null);
        }
        entity.setUuid(IdUtils.fastSimpleUUID());
        JsonResult<?> jsonResult = entity.checkValid(true, userService);
        if (jsonResult != null) {
            return jsonResult;
        }
        entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
        SysUser insert = userService.insert(entity);
        userRoleRelationService.insert(new SysUserRoleRelation(insert.getId(), 1L));
        redisCache.deleteObject(verifyKey);
        mailService.sendMimeMessage(entity.getEmail(), "用户反馈管理系统——注册成功", "恭喜" + entity.getUsername() + "注册成功，祝您使用愉快！", "text/plain;charset=UTF-8");
        return ResultTool.success(insert);
    }

    @GetMapping("checkUsername")
    public JsonResult<?> checkUsername(@RequestParam String username) {
        return ResultTool.success(userService.selectByUserName(username) == null);
    }

    @ApiOperation(value = "重置密码")
    @PostMapping("resetPassword")
    public JsonResult<?> resetPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        if (oldPassword.equals(newPassword)) {
            return ResultTool.fail(ResultCode.NEW_OLD_PASSWORD_CANNOT_EQUAL);
        }
        SysUser user = getUser();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            //验证通过
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            SysUser update = userService.update(user);
            mailService.sendMimeMessage(user.getEmail(), "用户反馈管理系统——密码重置", "您的密码已重置", "text/plain;charset=UTF-8");
            return ResultTool.success(update);
        } else {
            //验证失败
            return ResultTool.fail(ResultCode.USER_CREDENTIALS_ERROR);
        }
    }

    @ApiOperation(value = "更新用户信息")
    @PostMapping("update")
    public JsonResult<?> update(@RequestBody SysUser user) {
        SysUser localUser = getUser();
        if (!localUser.equals(user)) {
            return ResultTool.fail(ResultCode.NO_PERMISSION);
        }
        if (!localUser.getUsername().equals(user.getUsername()) && userService.selectByUserName(user.getUsername()) != null) {
            return ResultTool.fail(ResultCode.USER_NAME_ALREADY_EXIST);
        }
        SysUser update = userService.update(user);
        return ResultTool.success(update);
    }

    @ApiOperation(value = "生成随机用户")
    @GetMapping("generate")
    public JsonResult<?> generate() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid", IdUtils.fastSimpleUUID());
        jsonObject.put("uname", new KCNamer().getRandomName());
        jsonObject.put("uavatar", null);
        return ResultTool.success(jsonObject);
    }

}
