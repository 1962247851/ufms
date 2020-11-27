package top.ordinaryroad.ufms.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import top.ordinaryroad.ufms.common.service.ForeignKeyCrudService;
import top.ordinaryroad.ufms.entity.UfmsProduct;

/**
 * 产品服务类
 *
 * @author qq1962247851
 * @date 2020/10/27 18:42
 **/
public interface UfmsProductService extends ForeignKeyCrudService<UfmsProduct, Long> {
    /**
     * 根据名称找到产品
     *
     * @param name 产品名称
     * @return UfmsProduct
     */
    @Nullable
    UfmsProduct findByName(@NonNull String name);

    /**
     * 根据产品网站地址找到产品
     *
     * @param website 产品网站地址
     * @return UfmsProduct
     */
    @Nullable
    UfmsProduct findByWebsite(@NonNull String website);
}
