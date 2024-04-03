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
 * @description ReportDetailStressDO TODO report_detail_stress
 * @since 2024-02-10 14:52
 */
@Entity
@Table(name = "tbl_report_detail_stress")
@TableName("tbl_report_detail_stress")
@Getter
@Setter
@ToString
public class ReportDetailStressDO implements Serializable {

    @Id
    @TableId(value = "id")
    @Column(name = "id", length = 128, nullable = false)
    private String id;

    /**
     * 所属报告ID
     */
    @Column(length = 128, nullable = false)
    private String reportId;

    /**
     * 断言信息
     */
    @Lob
    private String assertInfo;

    /**
     * 错误请求数
     */
    private Long errorCount;

    /**
     * 错误百分比
     */
    private float errorPercentage;

    /**
     * 最大响应时间
     */
    private Long maxTime;

    /**
     * 平均响应时间
     */
    private float meanTime;

    /**
     * 最小响应时间
     */
    private Long minTime;

    /**
     * 每秒接收KB
     */
    @Column(name = "receive_k_b_per_second")
    private float receiveKBPerSecond;

    /**
     * 每秒发送KB
     */
    @Column(name = "sent_k_b_per_second")
    private float sentKBPerSecond;

    /**
     * 请求路径和参数
     */
    @Lob
    private String requestLocation;

    /**
     * 请求头
     */
    @Lob
    private String requestHeader;

    /**
     * 请求体
     */
    @Lob
    private String requestBody;

    /**
     * 每秒请求速率
     */
    private float requestRate;

    /**
     * 响应码
     */
    private String responseCode;

    /**
     * 响应体
     */
    @Lob
    private String responseData;

    /**
     * 响应头
     */
    @Lob
    private String responseHeader;


    /**
     * 采样次数编号
     */
    private Long samplerCount;

    /**
     * 请求名称
     */
    private String samplerLabel;

    /**
     * 请求时间戳
     */
    private Long sampleTime;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;
}
