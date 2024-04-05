package cn.skyln.web.model.REQ;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: lamella
 * @Date: 2022/09/04/10:51
 * @Description:
 */
@Data
public class UserLoginREQ {
    private String pwd;

    private String mail;

    @JsonProperty("safe_mode")
    private int safeMode;
}
