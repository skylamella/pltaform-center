package cn.skyln.web.model.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author skylamella
 * @since 2022-09-07
 */
@Data
public class CouponRecordVO {


    private String id;

    /**
     * 优惠券id
     */
    @JsonProperty("coupon_id")
    private String couponId;

    /**
     * 创建时间获得时间
     */
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date createTime;

    /**
     * 使用状态  可用 NEW,已使用USED,过期 EXPIRED;
     */
    @JsonProperty("use_state")
    private String useState;

    /**
     * 用户id
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * 用户昵称
     */
    @JsonProperty("user_name")
    private String userName;

    /**
     * 优惠券标题
     */
    @JsonProperty("coupon_title")
    private String couponTitle;

    /**
     * 开始时间
     */
    @JsonProperty("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date endTime;

    /**
     * 订单id
     */
    @JsonProperty("order_id")
    private String orderId;

    /**
     * 抵扣价格
     */
    private BigDecimal price;

    /**
     * 满多少才可以使用
     */
    @JsonProperty("condition_price")
    private BigDecimal conditionPrice;


}
