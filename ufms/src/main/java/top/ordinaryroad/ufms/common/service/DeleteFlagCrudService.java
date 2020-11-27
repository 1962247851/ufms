package top.ordinaryroad.ufms.common.service;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * 通用增删改查Service，实体类需要带有DeleteFlag字段
 *
 * @author qq1962247851
 * @date 2020/6/16 22:51
 */
public interface DeleteFlagCrudService<E, K> extends CrudService<E, K> {

    /**
     * 恢复
     *
     * @param id 主键
     * @return 是否成功
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @NotNull
    Boolean restore(@NotNull K id);

    /**
     * 根据主键查询isDeleted=false的数据
     *
     * @param id 主键
     * @return 实体
     */
    @Nullable
    E query(@NotNull K id);

    /**
     * 分页查询所有isDeleted=false的数据
     * <p>
     * page         页数，从1开始
     * limit        多少条数据，默认为10
     *
     * @param requestParams 参数
     * @return 分页的所有数据
     */
    Page<E> list(@RequestParam Map<String, Object> requestParams);

}
