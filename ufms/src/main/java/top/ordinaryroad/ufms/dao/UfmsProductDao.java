package top.ordinaryroad.ufms.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.ordinaryroad.ufms.entity.SysUser;
import top.ordinaryroad.ufms.entity.UfmsProduct;

import java.util.Optional;

/**
 * 产品数据库操作类
 *
 * @author qq1962247851
 * @date 2020/10/27 18:41
 **/
public interface UfmsProductDao extends JpaRepository<UfmsProduct, Long>, JpaSpecificationExecutor<UfmsProduct> {
    /**
     * 根据名称和网站模糊分页查询
     *
     * @param name     名称
     * @param website  网站地址
     * @param pageable 分页
     * @param user     创建者
     * @return 分页的所有数据
     */
    Page<UfmsProduct> findAllByNameLikeAndWebsiteLikeAndUser(String name, String website, SysUser user, Pageable pageable);

    /**
     * 根据名称模糊分页查询
     *
     * @param name     名称
     * @param pageable 分页
     * @param user     创建者
     * @return 分页的所有数据
     */
    Page<UfmsProduct> findAllByNameLikeAndUser(String name, SysUser user, Pageable pageable);

    /**
     * 根据网站模糊分页查询
     *
     * @param website  网站地址
     * @param pageable 分页
     * @param user     创建者
     * @return 分页的所有数据
     */
    Page<UfmsProduct> findAllByWebsiteLikeAndUser(String website, SysUser user, Pageable pageable);


    /**
     * 模糊分页查询
     *
     * @param user     创建者
     * @param pageable 分页
     * @return 分页的所有数据
     */
    Page<UfmsProduct> findAllByUser(SysUser user, Pageable pageable);

    /**
     * 模糊产品名称找到
     *
     * @param name 产品名称
     * @return 产品
     */
    Optional<UfmsProduct> findByName(String name);

    /**
     * 模糊产品网站地址找到
     *
     * @param website 产品网站地址
     * @return 产品
     */
    Optional<UfmsProduct> findByWebsite(String website);

}
