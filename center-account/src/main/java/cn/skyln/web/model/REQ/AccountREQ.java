package cn.skyln.web.model.REQ;

import lombok.Data;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author lamella
 * @description AccountREQ TODO
 * @since 2024-02-02 21:34
 */
@Data
public class AccountREQ {

    private String id;

    private String name;
    private String username;

    private String password;

    private String headImg;

    private String mail;

    private String secret;

    private String phone;

    private String auth;
    private String slogan;
    private Integer sex;
    private Integer points;

    private Date gmtCreate;

    private Date gmtModified;

    public boolean check() {
        return isNotBlank(username) && isNotBlank(password);
    }
}
