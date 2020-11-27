package top.ordinaryroad.ufms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import top.ordinaryroad.ufms.dao.UfmsProductDao;
import top.ordinaryroad.ufms.entity.SysUser;
import top.ordinaryroad.ufms.entity.UfmsProduct;
import top.ordinaryroad.ufms.service.UfmsProductService;

import java.util.Map;

/**
 * 产品服务实现类
 *
 * @author qq1962247851
 * @date 2020/10/27 18:44
 **/
@Service
public class UfmsProductServiceImpl implements UfmsProductService {

    private final UfmsProductDao dao;

    public UfmsProductServiceImpl(UfmsProductDao dao) {
        this.dao = dao;
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return 插入到数据库后的实体
     */
    @NotNull
    @Override
    public UfmsProduct insert(@NotNull UfmsProduct entity) {
        return dao.save(entity);
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @NotNull
    @Override
    public Boolean delete(@NotNull Long id) {
        dao.deleteById(id);
        return dao.findById(id).isEmpty();
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return 更新后的实体
     */
    @NotNull
    @Override
    public UfmsProduct update(@NotNull UfmsProduct entity) {
        return dao.saveAndFlush(entity);
    }

    /**
     * 根据主键找到数据
     *
     * @param id 主键
     * @return 实体
     */
    @Nullable
    @Override
    public UfmsProduct find(@NotNull Long id) {
        return dao.findById(id).orElse(null);
    }

    /**
     * 分页找到所有数据
     *
     * @param requestParams 参数
     * @return 分页的所有数据
     */
    @Override
    public Page<UfmsProduct> findAll(@RequestParam Map<String, Object> requestParams) {
        /*
          Request : {"args":"[1, 15, {page=1, limit=15, searchParams={\"name\":\"a.\",\"website\":\"\"}}]",
         */
        int page = Integer.parseInt(requestParams.get("page").toString()) - 1;
        int limit = Integer.parseInt(requestParams.get("limit").toString());
        long userId = Long.parseLong(requestParams.get("userId").toString());
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        if (requestParams.containsKey("searchParams")) {
            JSONObject searchParams = JSON.parseObject(requestParams.get("searchParams").toString());
            String name = "";
            String website = "";
            if (searchParams.containsKey("name") && searchParams.containsKey("website")) {
                name = "%" + searchParams.get("name") + "%";
                website = "%" + searchParams.get("website") + "%";
                return dao.findAllByNameLikeAndWebsiteLikeAndUser(name, website, new SysUser(userId), pageRequest);
            } else if (searchParams.containsKey("name")) {
                return dao.findAllByNameLikeAndUser(name, new SysUser(userId), pageRequest);
            } else if (searchParams.containsKey("website")) {
                return dao.findAllByWebsiteLikeAndUser(website, new SysUser(userId), pageRequest);
            } else {
                return dao.findAllByUser(new SysUser(userId), pageRequest);
            }
        }
        return dao.findAllByUser(new SysUser(userId), pageRequest);
    }

    /**
     * 根据关联的外键分页找到所有数据
     *
     * @param requestParams 参数
     * @return 分页的所有数据
     */
    @Override
    public Page<UfmsProduct> findAllByForeignKey(@NotNull Map<String, Object> requestParams) {
        return null;
    }

    /**
     * 根据名称找到产品
     *
     * @param name 产品名称
     * @return UfmsProduct
     */
    @Override
    public UfmsProduct findByName(@NonNull String name) {
        return dao.findByName(name).orElse(null);
    }

    /**
     * 根据网站地址找到产品
     *
     * @param website 产品网站地址
     * @return UfmsProduct
     */
    @Override
    public UfmsProduct findByWebsite(@NonNull String website) {
        return dao.findByWebsite(website).orElse(null);
    }
}
