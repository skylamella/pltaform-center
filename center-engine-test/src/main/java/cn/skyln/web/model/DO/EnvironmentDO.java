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
 * @description EnvironmentDO TODO
 * @since 2024-02-10 14:18
 */
@Entity
@Table(name = "tbl_environment")
@TableName("tbl_environment")
@Getter
@Setter
@ToString
public class EnvironmentDO implements Serializable {
    @Id
    @TableId(value = "id")
    @Column(name = "id", length = 128, nullable = false)
    private String id;

    @Column(length = 128, nullable = false)
    private String projectId;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String protocol;

    @Lob
    @Column(nullable = false)
    private String domain;

    @Column(nullable = false)
    private Integer port;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;
}
