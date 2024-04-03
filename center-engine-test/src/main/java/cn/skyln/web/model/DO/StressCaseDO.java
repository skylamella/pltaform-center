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
 * @description StressCaseDO TODO
 * @since 2024-02-10 14:34
 */
@Entity
@Table(name = "tbl_stress_case")
@TableName("tbl_stress_case")
@Getter
@Setter
@ToString
public class StressCaseDO implements Serializable {

    @Id
    @TableId(value = "id")
    @Column(name = "id", length = 128, nullable = false)
    private String id;

    /**
     * 项目id
     */
    @Column(length = 128, nullable = false)
    private String projectId;

    /**
     * 所属接口模块ID
     */
    @Column(length = 128, nullable = false)
    private String moduleId;

    /**
     * 环境id
     */
    @Column(length = 128, nullable = false)
    private String environmentId;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String description;

    /**
     * 响应断言
     */
    @Lob
    private String assertion;

    /**
     * 可变参数
     */
    @Lob
    private String relation;

    /**
     * 压测类型 [simple jmx]
     */
    @Column(length = 128, nullable = false)
    private String stressSourceType;

    /**
     * 压测参数
     */
    @Lob
    private String threadGroupConfig;

    /**
     * jmx文件地址
     */
    @Lob
    private String jmxUrl;

    /**
     * 接口路径
     */
    @Column(nullable = false)
    private String path;

    /**
     * 请求方法 [GET POST PUT PATCH DELETE HEAD OPTIONS]
     */
    @Column(nullable = false)
    private String method;

    /**
     * 查询参数
     */
    @Lob
    private String query;

    /**
     * 请求头
     */
    @Lob
    private String header;

    /**
     * 请求体
     */
    @Lob
    private String body;

    /**
     * 请求体格式 [raw form-data json]
     */
    private String bodyType;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;
}
