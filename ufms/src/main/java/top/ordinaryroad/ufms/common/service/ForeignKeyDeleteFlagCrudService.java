package top.ordinaryroad.ufms.common.service;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;


/**
 * 通用增删改查Service，带有外键关联，实体类需要带有DeleteFlag字段
 *
 * @author qq1962247851
 * @date 2020/6/16 22:51
 */
public interface ForeignKeyDeleteFlagCrudService<E, K, F> extends DeleteFlagCrudService<E, K> {

    /**
     * 根据关联的外键分页查询所有isDeleted=false的数据
     *
     * @param foreignKey 外键
     * @param page       页码数，从1开始
     * @param limit      多少条数据，默认为10
     * @return 分页的所有数据
     */
    Page<E> listByForeignKey(@NotNull F foreignKey, @NotNull Integer page, @NotNull Integer limit);

}
