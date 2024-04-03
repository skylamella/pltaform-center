package cn.skyln.web.feignClient;

import cn.skyln.util.JsonData;
import cn.skyln.web.feignClient.fallBack.CouponFeignServiceFallback;
import cn.skyln.web.model.DTO.CouponDTO;
import cn.skyln.web.model.DTO.LockCouponRecordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: lamella
 * @Date: 2022/09/23/21:44
 * @Description:
 */
@FeignClient(name = "skyln-coupon-service", fallback = CouponFeignServiceFallback.class)
public interface CouponFeignService {

    /**
     * 根据ID列表获取优惠券详情
     *
     * @param couponDTO CouponDTO
     * @return JsonData
     */
    @PostMapping("/api/v1/coupon_record/detail/list")
    JsonData queryUserCouponRecord(@RequestBody CouponDTO couponDTO);

    /**
     * 锁定优惠券
     *
     * @param lockCouponRecordDTO 锁定优惠券对象
     * @return JsonData
     */
    @PostMapping("/api/v1/coupon_record/lock_records")
    JsonData lockCouponRecords(@RequestBody LockCouponRecordDTO lockCouponRecordDTO);
}
