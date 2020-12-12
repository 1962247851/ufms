package top.ordinaryroad.ufms.entity;


import io.netty.util.internal.StringUtil;
import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.entity.MyJsonStringObject;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import top.ordinaryroad.ufms.service.SysUserService;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表(SysUser)实体类
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "uuid")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class SysUser extends MyJsonStringObject implements Serializable {
    private static final long serialVersionUID = 915478504870211231L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "sys_user_seq")
    @TableGenerator(name = "sys_user_seq",
            allocationSize = 1,//自动增长，设置为1
            pkColumnName = "sequence_name", //表里用来保存主键名字的字段
            valueColumnName = "sequence_next_hi_value",//表里用来保存主键值的字段
            pkColumnValue = "sys_user_id"//表里名字字段对应的值
    )
    private Long id;
    /**
     * UUID
     */
    @Column(unique = true, nullable = false)
    private String uuid;
    /**
     * 用户名
     */
    @Column(unique = true, nullable = false, length = 20)
    private String username;
    /**
     * 性别
     */
    @Column(columnDefinition = "bit(1) default 1")
    private Boolean sex = true;
    /**
     * 头像地址
     */
    @Column(length = 1000)
    private String avatar;
    /**
     * 邮箱
     */
    @Column(unique = true, length = 100)
    private String email;
    /**
     * 手机号码
     */
    @Column(unique = true, length = 11)
    private String phone;
    /**
     * 用户密码
     */
    @Column(length = 128)
    private String password;
    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;
    /**
     * 上一次登录时间
     */
    private Date lastLoginTime;
    /**
     * 账号是否可用。默认为1（可用）
     */
    @Column(columnDefinition = "bit(1) not null default 1")
    private Boolean enabled = true;
    /**
     * /**
     * 账号是否锁定。默认为1（没有锁定）
     */
    @Column(columnDefinition = "bit(1) not null default 1")
    private Boolean notLocked = true;
    /**
     * 是否过期。默认为1（没有过期）
     */
    @Column(columnDefinition = "bit(1) not null default 1")
    private Boolean notExpired = true;
    /**
     * 密码是否过期。默认为1（没有过期）
     */
    @Column(columnDefinition = "bit(1) not null default 1")
    private Boolean passwordNotExpired = true;
    /**
     * 创建时间
     */
    @Column(nullable = false)
    @CreatedDate
    private Date createTime;
    /**
     * 修改时间
     */
    @LastModifiedDate
    private Date updateTime;
    /**
     * 是否为管理员，否则是管理员下的普通用户
     */
    @Column(columnDefinition = "bit(1) not null default 0")
    private Boolean isAdmin = false;

    public SysUser(Long id) {
        this.id = id;
    }

    public SysUser(String uuid, String username, String avatar) {
        this.uuid = uuid;
        this.username = username;
        this.avatar = avatar;
    }

    public SysUser(String uuid) {
        this.uuid = uuid;
    }

    public SysUser(Long id, String uuid) {
        this.id = id;
        this.uuid = uuid;
    }

    @Nullable
    public JsonResult<?> checkValid(boolean isInsert, SysUserService service) {
        if (StringUtil.isNullOrEmpty(username)) {
            return ResultTool.fail(ResultCode.USERNAME_NOT_COMPLETE);
        }
        if (StringUtil.isNullOrEmpty(password)) {
            return ResultTool.fail(ResultCode.PASSWORD_NOT_COMPLETE);
        }
        SysUser byUsername = service.selectByUserName(username);
        if (isInsert) {
            if (byUsername != null) {
                return ResultTool.fail(ResultCode.USER_NAME_ALREADY_EXIST);
            }
//            if (!StringUtil.isNullOrEmpty(email) && service.findByEmail(email) != null) {
//                return ResultTool.fail(ResultCode.EMAIL_ALREADY_EXIST);
//            }
//            if (!StringUtil.isNullOrEmpty(phone) && service.findByPhone(phone) != null) {
//                return ResultTool.fail(ResultCode.PHONE_ALREADY_EXIST);
//            }
        } else {
            if (getId() == null) {
                return ResultTool.fail(ResultCode.ID_NOT_COMPLETE);
            }
            SysUser byId = service.queryById(id);
            if (byId == null) {
                return ResultTool.fail(ResultCode.USER_ACCOUNT_NOT_EXIST);
            }
            if (byUsername != null && !byUsername.getId().equals(id)) {
                return ResultTool.fail(ResultCode.USER_NAME_ALREADY_EXIST);
            }
            if (!new BCryptPasswordEncoder().matches(password, byId.getPassword())) {
                return ResultTool.fail(ResultCode.USER_CREDENTIALS_ERROR);
            }
            password = byId.getPassword();
        }
        return null;
    }

}