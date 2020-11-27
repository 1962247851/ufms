package top.ordinaryroad.ufms.entity.layuimini;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
public class SysMenu implements Serializable {
    // 复合主键要用这个注解
    @EmbeddedId
    private MenuKey key;
    private Long pid;
    private String icon;
    private String target;
    private Integer sort;
    private Boolean status;
    private String remark;
    @CreatedDate
    private Date create_at;
    @CreatedDate
    private Date update_at;
    private Date delete_at;
}