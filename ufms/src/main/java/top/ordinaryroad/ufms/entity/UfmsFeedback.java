package top.ordinaryroad.ufms.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.netty.util.internal.StringUtil;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;
import top.ordinaryroad.ufms.common.entity.JsonResult;
import top.ordinaryroad.ufms.common.entity.MyJsonStringObject;
import top.ordinaryroad.ufms.common.enums.ResultCode;
import top.ordinaryroad.ufms.common.utils.ResultTool;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


/**
 * 主反馈实体类
 *
 * @author qq1962247851
 * @date 2020/10/17 17:54
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class UfmsFeedback extends MyJsonStringObject {
    /**
     * 反馈ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ufms_feedback_seq")
    @TableGenerator(name = "ufms_feedback_seq",
            allocationSize = 1,//自动增长，设置为1
            pkColumnName = "sequence_name", //表里用来保存主键名字的字段
            valueColumnName = "sequence_next_hi_value",//表里用来保存主键值的字段
            pkColumnValue = "ufms_feedback_id"//表里名字字段对应的值
    )
    @NotNull
    private Long id;
    /**
     * 反馈UUID，上传图片时会用到
     */
    @Column(unique = true, nullable = false)
    @NotNull
    private String uuid;
    /**
     * 反馈时间
     */
    @CreatedDate
    @Column(nullable = false)
    @NotNull
    private Date createdDate;
    /**
     * 反馈内容
     */
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String content;
    /**
     * 反馈附图，JSON格式，相对地址（文件名）
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String pictures;
    /**
     * 反馈被点赞次数
     */
    @Column(nullable = false, columnDefinition = "int(11) default 0")
    @NotNull
    private Integer likeCount = 0;
    /**
     * 是否隐藏
     */
    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    @NotNull
    private Boolean isHidden = false;
    /**
     * 是否置顶
     */
    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    @NotNull
    private Boolean isTopping = false;
    /**
     * 是否锁定，禁止反馈
     */
    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    @NotNull
    private Boolean isLocked = false;
    /**
     * 是否标记为好问题
     */
    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    @NotNull
    private Boolean isRecommend = false;
    /**
     * 是否为管理员
     */
    @NotNull
    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    private Boolean isAdmin = false;
    /**
     * 图片list
     */
    @Nullable
    @Transient
    private List<String> images;
    /**
     * 主贴下的所有回复
     */
    @Nullable
    @Transient
    private Page<UfmsFeedback> replies;

    /**
     * 多个反馈对应一个产品
     */
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonProperty
    private UfmsProduct product;
    /**
     * 当主贴的时候为null，回复也可能为null
     */
    @Nullable
    @OneToOne
    @JsonIgnoreProperties({"parent", "original", "product", "replies"})
    private UfmsFeedback parent;
    /**
     * 主贴id
     */
    @Nullable
    @OneToOne
    @JsonIgnoreProperties({"replies"})
    private UfmsFeedback original;
    /**
     * 反馈者用户，开发者下的用户
     */
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(referencedColumnName = "uuid")
    private SysUser user;

    public JsonResult<?> checkValid() {
        return checkValid(false);
    }

    public JsonResult<?> checkValid(Boolean isInsert) {
        if (isInsert) {
            if (product == null || product.getId() == null) {
                return ResultTool.fail(ResultCode.PRODUCT_ID_NOT_NULL);
            }
            if (StringUtil.isNullOrEmpty(content)) {
                return ResultTool.fail(ResultCode.CONTENT_NOT_NULL);
            }
        }
//        else {
//            UfmsFeedback findById = feedbackService.find(getId());
//            if (findById != null) {
//            }
//        }
        return null;
    }

    @Nullable
    public List<String> getImages() {
        return JSON.parseArray(pictures, String.class);
    }

}
