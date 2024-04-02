package cn.skyln.web.feignClient.fallBack;

import cn.skyln.enums.BizCodeEnum;
import cn.skyln.util.JsonData;
import cn.skyln.web.feignClient.CouponFeignService;
import cn.skyln.web.model.DTO.NewUserCouponDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: lamella
 * @Date: 2022/09/10/20:03
 * @Description:
 */
@Slf4j
@Service
public class CouponFeignServiceFallback implements CouponFeignService {
    @Override
    public JsonData intiNewUserCoupon(NewUserCouponDTO newUserCouponDTO) {
        return JsonData.buildResult(BizCodeEnum.SYSTEM_ERROR);
    }
}
