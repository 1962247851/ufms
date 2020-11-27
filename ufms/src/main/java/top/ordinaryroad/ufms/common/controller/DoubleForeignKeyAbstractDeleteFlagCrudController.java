package top.ordinaryroad.ufms.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import top.ordinaryroad.ufms.common.entity.JsonResult;

/**
 * 增删改查控制器，实体类需要带有DeleteFlag字段
 *
 * @author qq1962247851
 * @date 2020/6/16 23:05
 */
public interface DoubleForeignKeyAbstractDeleteFlagCrudController<E, K, F> extends AbstractDeleteFlagCrudController<E, K> {

    /**
     * 根据fromForeignKey分页查询所有isDeleted=false的数据
     *
     * @param foreignKey 外键
     * @param offset     偏移量，从0开始
     * @param size       多少条数据，默认为10
     * @return 分页的所有数据
     */
    @GetMapping("listFrom")
    JsonResult<?> listFrom(F foreignKey, Integer offset, Integer size);

    /**
     * 根据fromForeignKey分页查询所有isDeleted=false的数据
     *
     * @param foreignKey 外键
     * @param offset     偏移量，从0开始
     * @param size       多少条数据，默认为10
     * @return 分页的所有数据
     */
    @GetMapping("listTo")
    JsonResult<?> listTo(F foreignKey, Integer offset, Integer size);

}
