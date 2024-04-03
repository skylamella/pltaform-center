package cn.skyln.web.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: lamella
 * @Date: 2022/09/19/22:08
 * @Description:
 */
@Data
public class LockCouponRecordDTO {

    @JsonProperty("lock_coupon_record_ids")
    private List<String> lockCouponRecordIds;

    @JsonProperty("order_out_trade_no")
    private String orderOutTradeNo;
}
