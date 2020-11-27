package top.ordinaryroad.ufms.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import top.ordinaryroad.ufms.common.entity.JsonResult;

import java.util.Map;

/**
 * 增删改查控制器，带有外键关联
 *
 * @author qq1962247851
 * @date 2020/6/16 23:05
 */
public interface ForeignKeyAbstractCrudController<E, K> extends AbstractCrudController<E, K> {

    /**
     * 根据关联的外键分页找到所有数据
     *
     * @param requestParams 请求参数
     * @return 分页的所有数据
     */
    @GetMapping("findAllByForeignKey")
    JsonResult<?> findAllByForeignKey(Map<String, Object> requestParams);

}
