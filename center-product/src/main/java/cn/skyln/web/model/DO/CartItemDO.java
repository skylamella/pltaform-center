package cn.skyln.web.model.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author skylamella
 * @since 2022-10-23
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "tbl_cart_item")
@TableName("tbl_cart_item")
public class CartItemDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @TableId(value = "id")
    @Column(name = "id", length = 128, nullable = false)
    private String id;

    /**
     * 购物车ID
     */
    private String cartId;

    /**
     * 商品ID
     */
    @JsonProperty("product_id")
    private String productId;

    /**
     * 购买数量
     */
    @JsonProperty("buy_num")
    private Integer buyNum;

    /**
     * 商品标题
     */
    @JsonProperty("product_title")
    private String productTitle;

    /**
     * 商品图
     */
    @JsonProperty("product_img")
    private String productImg;

    /**
     * 商品单价
     */
    private BigDecimal amount;


}
