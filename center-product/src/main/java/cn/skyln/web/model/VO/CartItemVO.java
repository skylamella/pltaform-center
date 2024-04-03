package cn.skyln.web.model.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Author: lamella
 * @Date: 2022/09/12/14:58
 * @Description: 购物项
 */
public class CartItemVO {
    /**
     * 商品ID
     */
    @Setter
    @Getter
    @JsonProperty("product_id")
    private String productId;

    /**
     * 商品数量
     */
    @Setter
    @Getter
    @JsonProperty("buy_num")
    private Integer buyNum;

    /**
     * 商品标题
     */
    @Setter
    @Getter
    @JsonProperty("product_title")
    private String productTitle;

    /**
     * 商品封面图
     */
    @Setter
    @Getter
    @JsonProperty("product_img")
    private String productImg;

    /**
     * 商品单价
     */
    @Setter
    @Getter
    private BigDecimal amount;

    /**
     * 商品总价，单价*数量
     */
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    /**
     * 是否删除标识
     */
    @Setter
    @Getter
    @JsonProperty("del_statue")
    private boolean delStatue;

    public BigDecimal getTotalAmount() {
        return this.amount.multiply(new BigDecimal(this.buyNum));
    }

}
