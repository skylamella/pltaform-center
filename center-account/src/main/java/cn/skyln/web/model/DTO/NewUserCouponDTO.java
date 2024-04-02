package cn.skyln.web.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: lamella
 * @Date: 2022/09/10/20:30
 * @Description:
 */
@Data
@Builder
public class NewUserCouponDTO {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_name")
    private String userName;
}
