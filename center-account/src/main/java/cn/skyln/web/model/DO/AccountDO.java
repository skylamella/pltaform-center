package cn.skyln.web.model.DO;

import cn.skyln.web.model.REQ.AccountREQ;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import static cn.skyln.util.SecurityUtil.MD5WithSecret;
import static cn.skyln.util.SecurityUtil.generateSaltSecret;

/**
 * @author lamella
 * @description Account TODO
 * @since 2024-01-25 22:55
 */
@Entity
@Table(name = "tbl_account")
@TableName("tbl_account")
@Getter
@Setter
@ToString
public class AccountDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @TableId(value = "id")
    @Column(name = "id", length = 128, nullable = false)
    private String id;

    /**
     * 昵称
     */
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String pwd;

    @Lob
    private String headImg;

    @Column
    private String mail;

    @Column(nullable = false)
    private String secret;

    @Column(length = 11)
    private String phone;

    @Column(nullable = false)
    private String auth;

    /**
     * 用户签名
     */
    private String slogan;

    /**
     * 0表示女，1表示男
     */
    @Column(nullable = false)
    private Integer sex;

    /**
     * 积分
     */
    private Integer points;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;

    public AccountDO() {
    }

    public AccountDO(AccountREQ accountREQ) {
        BeanUtils.copyProperties(accountREQ, this);
        this.secret = generateSaltSecret(this.username);
        this.pwd = MD5WithSecret(accountREQ.getPassword(), this.secret);
        this.id = (accountREQ.getId());
    }
}
