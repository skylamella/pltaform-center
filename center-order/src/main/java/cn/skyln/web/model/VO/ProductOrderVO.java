package cn.skyln.web.model.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author skylamella
 * @since 2022-09-12
 */
@Data
public class ProductOrderVO {

    private String id;

    /**
     * 订单唯⼀标识
     */
    private String outTradeNo;

    /**
     * NEW	未⽀付订单,PAY已经⽀付订单,CANCEL超时取消订单
     */
    private String state;

    /**
     * 订单⽣成时间
     */
    @JsonProperty("gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @JsonProperty("gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 订单总⾦额
     */
    private BigDecimal totalAmount;

    /**
     * 订单实际⽀付价格
     */
    private BigDecimal payAmount;

    /**
     * ⽀付类型，微信-银⾏-⽀付宝
     */
    private String payType;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String headImg;

    /**
     * ⽤户	id
     */
    private String userId;

    /**
     * 0表示未删除，	1表示已经删除
     */
    private Integer del;

    /**
     * 订单类型 DAILY普通单，PROMOTION促销订单
     */
    private String orderType;

    /**
     * 收货地址 json存储
     */
    private String receiverAddress;

    List<OrderItemVO> orderItemVOList;

}
