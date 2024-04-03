package cn.skyln.web.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: lamella
 * @Date: 2022/09/21/22:07
 * @Description:
 */
@Data
public class OrderItemDTO {

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("buy_num")
    private int buyNum;
}
