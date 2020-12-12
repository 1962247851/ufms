package top.ordinaryroad.ufms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.ordinaryroad.ufms.entity.SysUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysUserDao extends JpaRepository<SysUser, Long> {

    /**
     * 根据用户名找到用户
     *
     * @param username 用户名
     * @return 实体类
     */
    Optional<SysUser> findByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email userName
     * @return user
     */
    Optional<SysUser> findByEmail(String email);

    /**
     * 根据手机号查询用户
     *
     * @param phone userName
     * @return user
     */
    Optional<SysUser> findByPhone(String phone);

    @Query(value = "select * from sys_user limit ?#{#limit} offset ?#{#offset}", nativeQuery = true)
    List<SysUser> queryAllByLimit(@Param("offset") Long offset, @Param("limit") Long limit);
    /**
     * 根据uuid查询用户
     *
     * @param uuid uuid
     * @return user
     */
    Optional<SysUser> findByUuid(String uuid);
}
