package top.ordinaryroad.ufms.common.service;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;


/**
 * 通用增删改查Service，带有外键关联
 *
 * @author qq1962247851
 * @date 2020/6/16 22:51
 */
public interface DoubleForeignKeyCrudService<E, K, F> extends CrudService<E, K> {

    /**
     * 根据fromForeignKey分页找到所有数据
     *
     * @param foreignKey 外键
     * @param offset     偏移量，从0开始
     * @param size       多少条数据，默认为10
     * @return 分页的所有数据
     */
    Page<E> findAllFrom(@NotNull F foreignKey, @NotNull Integer offset, @NotNull Integer size);

    /**
     * 根据fromForeignKey分页找到所有数据
     *
     * @param foreignKey 外键
     * @param offset     偏移量，从0开始
     * @param size       多少条数据，默认为10
     * @return 分页的所有数据
     */
    Page<E> findAllTo(@NotNull F foreignKey, @NotNull Integer offset, @NotNull Integer size);

}
