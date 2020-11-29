package top.ordinaryroad.ufms.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.ordinaryroad.ufms.common.entity.MyJsonStringObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表(SysUser)实体类
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
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
    @NotNull
    private String uuid;
    /**
     * 用户名
     */
    @Column(unique = true, nullable = false, length = 20)
    private String name;
    /**
     * 性别
     */
    @Column(columnDefinition = "bit(1) default 1", nullable = false)
    private Boolean sex = true;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 头像地址
     */
    @NotNull
    @Column(length = 1000, nullable = false)
    private String avatar;
    /**
     * 邮箱
     */
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    /**
     * 手机号码
     */
    @Column(unique = true, length = 11)
    private String phone;
    /**
     * 用户密码
     */
    @Column(nullable = false, length = 64)
    private String password;
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

    public SysUser(Long id) {
        this.id = id;
    }

}