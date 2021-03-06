package top.ordinaryroad.ufms.service;


import org.jetbrains.annotations.Nullable;
import top.ordinaryroad.ufms.entity.SysUser;

import java.util.List;

/**
 * 用户表(SysUser)表服务接口
 */
public interface SysUserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Nullable
    SysUser queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysUser> queryAllByLimit(Long offset, Long limit);

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    SysUser insert(SysUser sysUser);

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    SysUser update(SysUser sysUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param userName userName
     * @return user
     */
    @Nullable
    SysUser selectByUserName(String userName);

    /**
     * 根据uuid查询用户
     *
     * @param uuid uuid
     * @return user
     */
    @Nullable
    SysUser selectByUuid(String uuid);

    /**
     * 根据邮箱查询用户
     *
     * @param email email
     * @return user
     */
    @Nullable
    SysUser selectByEmail(String email);

}