package top.ordinaryroad.ufms.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import top.ordinaryroad.ufms.common.entity.JsonResult;

import java.util.Map;

/**
 * 增删改查控制器，实体类需要带有DeleteFlag字段
 *
 * @author qq1962247851
 * @date 2020/6/16 23:05
 */
public interface AbstractDeleteFlagCrudController<E, K> extends AbstractCrudController<E, K> {

    /**
     * 恢复
     *
     * @param id 主键
     * @return JsonString
     */
    @PostMapping("restore")
    JsonResult<?> restore(K id);

    /**
     * 根据主键查询isDeleted=false的数据
     *
     * @param id 主键
     * @return JsonString
     */
    @GetMapping("query")
    JsonResult<?> query(K id);

    /**
     * 分页查询所有isDeleted=false的数据
     * <p>
     * page         页码数，从1开始
     * limit        多少条数据，默认为10
     *
     * @param requestParams 搜索参数
     * @return JsonString
     */
    @GetMapping("list")
    JsonResult<?> list(Map<String, Object> requestParams);

}
