package cn.skyln.web.feignClient.fallBack;

import cn.skyln.enums.BizCodeEnum;
import cn.skyln.util.JsonData;
import cn.skyln.web.feignClient.UserFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: lamella
 * @Date: 2022/09/22/22:09
 * @Description:
 */
@Slf4j
@Service
public class UserFeignServiceFallback implements UserFeignService {
    @Override
    public JsonData getOneAddress(String addressId) {
        return JsonData.buildResult(BizCodeEnum.SYSTEM_ERROR);
    }
}
