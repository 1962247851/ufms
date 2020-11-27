package top.ordinaryroad.ufms.common.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import top.ordinaryroad.ufms.common.entity.JsonResult;

import java.util.List;
import java.util.Map;

/**
 * 增删改查控制器
 *
 * @author qq1962247851
 * @date 2020/6/16 23:05
 */
public interface AbstractCrudController<E, K> {

    /**
     * 新增
     *
     * @param entity 实体
     * @return JsonString
     */
    @PutMapping(value = "insert")
    JsonResult<?> insert(E entity);

    /**
     * 删除
     *
     * @param ids 主键列表
     * @return JsonString
     */
    @DeleteMapping("delete")
    JsonResult<?> delete(List<K> ids);

    /**
     * 更新
     *
     * @param entity 实体
     * @return JsonString
     */
    @PostMapping("update")
    JsonResult<?> update(E entity);

    /**
     * 根据主键找到数据
     *
     * @param id 主键
     * @return JsonString
     */
    @GetMapping("find")
    JsonResult<?> find(K id);

    /**
     * 分页找到所有数据
     *
     * @param requestParams 参数
     * @return JsonString
     */
    @GetMapping("findAll")
    JsonResult<?> findAll(Map<String, Object> requestParams);

}
