package cn.skyln.web.feignClient;

import cn.skyln.util.JsonData;
import cn.skyln.web.feignClient.impl.ProductOrderFeignServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lamella
 * @description ProductOrderFeignService TODO
 * @since 2024-04-03 20:43
 */
@FeignClient(name = "center-order-service", fallback = ProductOrderFeignServiceFallback.class)
public interface ProductOrderFeignService {

    /**
     * 查询订单状态
     *
     * @param outTradeNo 订单号
     * @return JsonData
     */
    @GetMapping("/api/v1/order/query_state")
    JsonData queryProductOrderState(@RequestParam("out_trade_no") String outTradeNo);
}
