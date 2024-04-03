package cn.skyln.web.feignClient.impl;

import cn.skyln.enums.BizCodeEnum;
import cn.skyln.util.JsonData;
import cn.skyln.web.feignClient.ProductOrderFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lamella
 * @description ProductOrderFeignServiceFallback TODO
 * @since 2024-04-03 20:43
 */
@Slf4j
@Service
public class ProductOrderFeignServiceFallback implements ProductOrderFeignService {
    @Override
    public JsonData queryProductOrderState(String outTradeNo) {
        return JsonData.buildResult(BizCodeEnum.SYSTEM_ERROR);
    }
}
