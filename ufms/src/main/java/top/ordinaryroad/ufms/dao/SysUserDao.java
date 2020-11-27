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

    Optional<SysUser> findByName(String name);

    @Query(value = "select * from sys_user limit ?#{#limit} offset ?#{#offset}", nativeQuery = true)
    List<SysUser> queryAllByLimit(@Param("offset") Long offset, @Param("limit") Long limit);
}
