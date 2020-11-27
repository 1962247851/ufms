package top.ordinaryroad.ufms.entity;

import io.netty.util.internal.StringUtil;
import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.entity.MyJsonStringObject;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.ResultTool;
import top.ordinaryroad.ufms.service.UfmsProductService;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 产品实体类
 *
 * @author qq1962247851
 * @date 2020/10/17 11:53
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class UfmsProduct extends MyJsonStringObject {
    /**
     * 产品ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ufms_product_seq")
    @TableGenerator(name = "ufms_product_seq",
            allocationSize = 1,//自动增长，设置为1
            pkColumnName = "sequence_name", //表里用来保存主键名字的字段
            valueColumnName = "sequence_next_hi_value",//表里用来保存主键值的字段
            pkColumnValue = "ufms_product_id"//表里名字字段对应的值
    )
    @NotNull
    private Long id;
    /**
     * 产品UUID，上传图片时会用到
     */
    @Column(unique = true, nullable = false)
    @NotNull
    private String uuid;
    /**
     * 产品LOGO，相对地址
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @Nullable
    private String logo;
    /**
     * 封面头图，相对地址
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @Nullable
    private String banner;
    /**
     * 产品名称
     */
    @Column(unique = true, nullable = false)
    @NotNull
    private String name;
    /**
     * 产品官网
     */
    @Column(unique = true)
    @Nullable
    private String website;
    /**
     * 产品密钥，验证API接口等
     */
    @Column(unique = true)
    @NotNull
    private String privateKey;

    /**
     * 多个产品对应一个用户
     */
    @ManyToOne
    private SysUser user;

    public UfmsProduct(Long id) {
        this.id = id;
    }

    public JsonResult<?> checkValid(UfmsProductService productService) {
        return checkValid(productService, false);
    }

    public JsonResult<?> checkValid(UfmsProductService productService, Boolean isInsert) {
        if (isInsert) {
            if (productService.findByName(getName()) != null) {
                return ResultTool.fail(ResultCode.NAME_ALREADY_EXIST);
            }
            if (!StringUtil.isNullOrEmpty(getWebsite())) {
                if (productService.findByWebsite(getWebsite()) != null) {
                    return ResultTool.fail(ResultCode.WEBSITE_ALREADY_EXIST);
                }
            }
        } else {
            UfmsProduct findById = productService.find(getId());
            if (findById != null) {
                if (!findById.getName().equals(getName())) {
                    if (productService.findByName(getName()) != null) {
                        return ResultTool.fail(ResultCode.NAME_ALREADY_EXIST);
                    }
                }
                if (!StringUtil.isNullOrEmpty(getWebsite())) {
                    if (!getWebsite().equals(findById.getWebsite())) {
                        if (productService.findByWebsite(getWebsite()) != null) {
                            return ResultTool.fail(ResultCode.WEBSITE_ALREADY_EXIST);
                        }
                    }
                }
            }
        }
        return null;
    }
}
