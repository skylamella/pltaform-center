package cn.skyln.web.model.DO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lamella
 * @description ProjectDO TODO
 * @since 2024-02-10 13:20
 */
@Entity
@Table(name = "tbl_project")
@TableName("tbl_project")
@Getter
@Setter
@ToString
public class ProjectDO implements Serializable {

    @Id
    @TableId(value = "id")
    @Column(name = "id", length = 128, nullable = false)
    private String id;

    @Column(length = 128, nullable = false)
    private String projectAdminId;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;
}
