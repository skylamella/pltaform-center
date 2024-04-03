package cn.skyln.web.model.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
@Table(name = "tbl_product_order_item")
@TableName("tbl_product_order_item")
public class ProductOrderItemDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @TableId(value = "id")
    @Column(name = "id", length = 128, nullable = false)
    private String id;

    /**
     * 订单号
     */
    private String productOrderId;

    private String outTradeNo;

    /**
     * 产品id
     */
    private String productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图⽚
     */
    private String productImg;

    /**
     * 购买数量
     */
    private Integer buyNum;

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
     * 购物项商品总价格
     */
    private BigDecimal totalAmount;

    /**
     * 购物项商品单价
     */
    private BigDecimal amount;


}
