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
 * @description ReportDO TODO report
 * @since 2024-02-10 14:45
 */
@Entity
@Table(name = "tbl_report")
@TableName("tbl_report")
@Getter
@Setter
@ToString
public class ReportDO implements Serializable {
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
     * 压测用例ID
     */
    @Column(length = 128, nullable = false)
    private String caseId;

    /**
     * 报告类型
     */
    @Column(nullable = false)
    private String type;

    /**
     * 报告名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 执行状态
     */
    @Column(nullable = false)
    private String executeState;

    /**
     * 执行状态
     */
    @Lob
    private String summary;

    /**
     * 开始时间
     */
    @Column(nullable = false)
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(nullable = false)
    private Date endTime;

    /**
     * 消耗时间
     */
    @Column(nullable = false)
    private Long expandTime;

    /**
     * 步骤数量
     */
    @Column(nullable = false)
    private Long quantity;

    /**
     * 通过数量
     */
    @Column(nullable = false)
    private Long passQuantity;

    /**
     * 失败数量
     */
    @Column(nullable = false)
    private Long failQuantity;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;
}
