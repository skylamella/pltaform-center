package cn.skyln.web.model.REQ;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: lamella
 * @Date: 2022/09/12/21:18
 * @Description:
 */
@Data
public class ConfirmOrderRequest {

    /**
     * 购物车使用的优惠券
     * <p>
     * 注意：如果传空或者小于0，则不用优惠券
     */
    @JsonProperty("coupon_record_id_list")
    private List<String> couponRecordIdList;

    /**
     * 最终购买的商品列表
     * <p>
     * 传递ID，购买数量从购物车中读取
     */
    @JsonProperty("product_id_list")
    private List<String> productIdList;

    /**
     * 支付方式
     */
    @JsonProperty("pay_type")
    private String payType;

    /**
     * 客户端类型
     */
    @JsonProperty("client_type")
    private String clientType;

    /**
     * 收货地址ID
     */
    @JsonProperty("address_id")
    private String addressId;

    /**
     * 总价格，前端传递，后端需要验价
     */
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    /**
     * 实际支付的价格
     * <p>
     * 如果用了优惠券，则是减去优惠券的价格
     * 如果没有使用优惠券，则与totalAmount一致
     */
    @JsonProperty("real_pay_amount")
    private BigDecimal realPayAmount;

    /**
     * 防重令牌
     */
    private String token;

}
