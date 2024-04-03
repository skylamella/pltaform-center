package cn.skyln.web.service;

import cn.skyln.enums.coupon.CouponCategoryEnum;
import cn.skyln.util.JsonData;
import cn.skyln.web.model.DTO.NewUserCouponDTO;

import java.util.Map;

/**
 * @author lamella
 * @description CouponService TODO
 * @since 2024-04-03 19:34
 */
public interface CouponService {

    /**
     * 分页查询优惠券
     *
     * @param page 第几页
     * @param size 一页显示几条
     * @return Map
     */
    Map<String, Object> pageCouponActivity(int page, int size);

    /**
     * 领取优惠券接口
     *
     * @param couponId  优惠券ID
     * @param promotion 优惠券类型
     * @return JsonData
     */
    JsonData addCoupon(String couponId, CouponCategoryEnum promotion);

    /**
     * 新用户注册发放优惠券
     *
     * @param newUserCouponDTO 新用户注册领券对象
     * @return JsonData
     */
    JsonData intiNewUserCoupon(NewUserCouponDTO newUserCouponDTO);
}
