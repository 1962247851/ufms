package top.ordinaryroad.ufms.common.service;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * 通用增删改查Service
 *
 * @author qq1962247851
 * @date 2020/6/16 22:51
 */
public interface CrudService<E, K> {

    /**
     * 插入
     *
     * @param entity 实体
     * @return 插入到数据库后的实体
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @NotNull
    E insert(@NotNull E entity);

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @NotNull
    Boolean delete(@NotNull K id);

    /**
     * 更新
     *
     * @param entity 实体
     * @return 更新后的实体
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @NotNull
    E update(@NotNull E entity);

    /**
     * 根据主键找到数据
     *
     * @param id 主键
     * @return 实体
     */
    @Nullable
    E find(@NotNull K id);

    /**
     * 分页找到所有数据
     *
     * @param requestParams 参数
     * @return 分页的所有数据
     */
    Page<E> findAll(@RequestParam Map<String, Object> requestParams);

    /**
     * 分页找到所有数据
     *
     * @param specification specification
     * @param pageable      分页
     * @return 分页的所有数据
     */
    Page<E> findAll(@NotNull Specification<E> specification, @NotNull Pageable pageable);

}
