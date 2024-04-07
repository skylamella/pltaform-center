package cn.skyln.web.model.DO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author skylamella
 * @since 2022-09-12
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "tbl_product_order")
@TableName("tbl_product_order")
public class ProductOrderDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @TableId(value = "id")
    @Column(name = "id", length = 128, nullable = false)
    private String id;

    /**
     * 订单唯⼀标识
     */
    private String outTradeNo;

    /**
     * NEW	未⽀付订单,PAY已经⽀付订单,CANCEL超时取消订单
     */
    private String state;

    /**
     * 订单⽣成时间
     */
    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;

    /**
     * 订单总⾦额
     */
    private BigDecimal totalAmount;

    /**
     * 订单实际⽀付价格
     */
    private BigDecimal payAmount;

    /**
     * ⽀付类型，微信-银⾏-⽀付宝
     */
    private String payType;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String headImg;

    /**
     * ⽤户	id
     */
    private String userId;

    /**
     * 0表示未删除，	1表示已经删除
     */
    private Integer del;

    /**
     * 订单类型 DAILY普通单，PROMOTION促销订单
     */
    private String orderType;

    /**
     * 收货地址 json存储
     */
    private String receiverAddress;


}
