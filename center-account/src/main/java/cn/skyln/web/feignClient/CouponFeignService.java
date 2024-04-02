package cn.skyln.web.feignClient;

import cn.skyln.util.JsonData;
import cn.skyln.web.feignClient.fallBack.CouponFeignServiceFallback;
import cn.skyln.web.model.DTO.NewUserCouponDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author lamella
 * @description CouponFeignService TODO
 * @since 2024-04-02 23:04
 */
@FeignClient(name = "center-coupon-service", fallback = CouponFeignServiceFallback.class)
public interface CouponFeignService {

    /**
     * 新用户注册发放优惠券
     *
     * @param newUserCouponDTO 新用户注册领券对象
     * @return JsonData
     */
    @PostMapping("/api/v1/coupon/add/new_user")
    JsonData intiNewUserCoupon(NewUserCouponDTO newUserCouponDTO);
}
