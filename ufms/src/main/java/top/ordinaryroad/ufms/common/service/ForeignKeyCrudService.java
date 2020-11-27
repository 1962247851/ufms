package top.ordinaryroad.ufms.common.service;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * 通用增删改查Service，带有外键关联
 *
 * @author qq1962247851
 * @date 2020/6/16 22:51
 */
public interface ForeignKeyCrudService<E, K> extends CrudService<E, K> {

    /**
     * 根据关联的外键分页找到所有数据
     *
     * @param requestParams 参数
     * @return 分页的所有数据
     */
    Page<E> findAllByForeignKey(@NotNull Map<String, Object> requestParams);

}
