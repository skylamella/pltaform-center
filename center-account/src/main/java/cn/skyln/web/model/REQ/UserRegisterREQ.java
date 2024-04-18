package cn.skyln.web.model.REQ;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: lamella
 * @Date: 2022/09/04/10:51
 * @Description:
 */
@Data
public class UserRegisterREQ {
    private String pwd;

    @JsonProperty("re_pwd")
    private String rePwd;

    private String name;
    private String username;

    @JsonProperty("head_img")
    private String headImg;

    private String slogan;

    private Integer sex;

    private String mail;

    private String code;
}
