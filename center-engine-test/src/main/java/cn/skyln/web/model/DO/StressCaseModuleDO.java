package cn.skyln.web.model.DO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author lamella
 * @description StressCaseModuleDO TODO stress_case_module
 * @since 2024-02-10 14:21
 */
@Entity
@Table(name = "tbl_stress_case_module")
@TableName("tbl_stress_case_module")
@Getter
@Setter
@ToString
public class StressCaseModuleDO {
    @Id
    @TableId(value = "id")
    @Column(name = "id", length = 128, nullable = false)
    private String id;

    @Column(length = 128, nullable = false)
    private String projectId;

    @Column(nullable = false, unique = true)
    private String name;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;
}
