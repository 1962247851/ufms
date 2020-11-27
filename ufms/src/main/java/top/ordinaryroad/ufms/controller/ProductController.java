package top.ordinaryroad.ufms.controller;

import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;
import top.ordinaryroad.ufms.common.controller.AbstractCrudController;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.IdUtils;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import top.ordinaryroad.ufms.entity.UfmsProduct;
import top.ordinaryroad.ufms.service.UfmsProductService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 产品控制器
 *
 * @author qq1962247851
 * @date 2020/10/27 18:31
 **/
@Api
@RestController
@RequestMapping(value = "/api/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController extends BaseUserController implements AbstractCrudController<UfmsProduct, Long> {

    private final UfmsProductService productService;

    public ProductController(UfmsProductService service, HttpSession session) {
        this.productService = service;
        this.session = session;
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return JsonString
     */
    @Override
    public JsonResult<?> insert(@RequestBody UfmsProduct entity) {

        JsonResult<?> jsonResult = entity.checkValid(productService, true);
        if (jsonResult != null) {
            return jsonResult;
        }


        if (StringUtils.isEmpty(entity.getUuid())) {
            entity.setUuid(IdUtils.fastSimpleUUID());
        }
        entity.setPrivateKey(IdUtils.fastSimpleUUID());
        entity.setUser(getUser());
        return ResultTool.success(productService.insert(entity));
    }

    /**
     * 删除
     *
     * @param ids 主键列表
     * @return JsonString
     */
    @Override
    public JsonResult<?> delete(@RequestBody List<Long> ids) {
        ids.forEach(id -> {
            UfmsProduct find = productService.find(id);
            if (find != null && find.getUser().equals(getUser())) {
                productService.delete(id);
            }
        });
        return ResultTool.success();
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return JsonString
     */
    @Override
    public JsonResult<?> update(@RequestBody UfmsProduct entity) {
        UfmsProduct find = productService.find(entity.getId());
        if (find != null) {
            JsonResult<?> jsonResult = entity.checkValid(productService);
            if (jsonResult != null) {
                return jsonResult;
            }

            if (find.getUser().equals(getUser())) {
                return ResultTool.success(productService.update(entity));
            } else {
                return ResultTool.fail(ResultCode.NO_PERMISSION);
            }
        } else {
            return ResultTool.fail(ResultCode.DATA_NOT_EXIST);
        }
    }

    /**
     * 根据主键找到数据
     *
     * @param id 主键
     * @return JsonString
     */
    @Override
    public JsonResult<?> find(@RequestParam Long id) {
        UfmsProduct ufmsProduct = productService.find(id);
        if (ufmsProduct == null) {
            return ResultTool.fail(ResultCode.DATA_NOT_EXIST);
        }
        return ResultTool.success(ufmsProduct);
    }

    /**
     * 分页找到所有数据
     *
     * @param requestParams 参数
     * @return JsonString
     */
    @Override
    public JsonResult<?> findAll(@RequestParam Map<String, Object> requestParams) {
        requestParams.put("userId", (getUser()).getId());
        Page<UfmsProduct> productPage = productService.findAll(requestParams);
        return ResultTool.success(productPage);
    }

}
