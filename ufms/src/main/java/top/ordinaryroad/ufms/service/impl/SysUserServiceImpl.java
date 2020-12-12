package top.ordinaryroad.ufms.service.impl;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import top.ordinaryroad.ufms.dao.SysUserDao;
import top.ordinaryroad.ufms.entity.SysUser;
import top.ordinaryroad.ufms.service.SysUserService;

import java.util.List;

/**
 * 用户表(SysUser)表服务实现类
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    private final SysUserDao sysUserDao;

    public SysUserServiceImpl(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysUser queryById(Long id) {
        return sysUserDao.findById(id).orElse(null);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<SysUser> queryAllByLimit(Long offset, Long limit) {
        return sysUserDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public SysUser insert(SysUser sysUser) {
        return sysUserDao.save(sysUser);
    }

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public SysUser update(SysUser sysUser) {
        return sysUserDao.saveAndFlush(sysUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        try {
            sysUserDao.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return sysUserDao.findById(id).isPresent();
    }

    @Override
    public SysUser selectByUserName(String userName) {
        return sysUserDao.findByUsername(userName).orElse(null);
    }

    /**
     * 根据uuid查询用户
     *
     * @param uuid uuid
     * @return user
     */
    @Nullable
    @Override
    public SysUser selectByUuid(String uuid) {
        return sysUserDao.findByUuid(uuid).orElse(null);
    }

    /**
     * 根据邮箱查询用户
     *
     * @param email email
     * @return user
     */
    @Nullable
    @Override
    public SysUser selectByEmail(String email) {
        return sysUserDao.findByEmail(email).orElse(null);
    }

}