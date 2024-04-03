package cn.skyln.web.model.REQ;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: lamella
 * @Date: 2022/09/12/15:35
 * @Description:
 */
@Data
public class CartItemRequest {

    @JsonProperty("product_id")
    private String productId;

    /**
     * 购买数量
     */
    @JsonProperty("buy_num")
    private Integer buyNum;

}
