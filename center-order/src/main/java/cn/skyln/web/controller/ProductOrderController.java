package cn.skyln.web.controller;


import cn.skyln.constant.CacheKey;
import cn.skyln.constant.TimeConstant;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.interceptor.LoginInterceptor;
import cn.skyln.model.LoginUser;
import cn.skyln.util.CommonUtils;
import cn.skyln.util.JsonData;
import cn.skyln.web.model.REQ.ConfirmOrderRequest;
import cn.skyln.web.model.REQ.RepayOrderRequest;
import cn.skyln.web.service.ProductOrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author skylamella
 * @since 2022-09-12
 */
@RestController
@RequestMapping("/api/v1/order/")
@Slf4j
public class ProductOrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/confirm")
    public void confirmOrder(@RequestBody ConfirmOrderRequest confirmOrderRequest,
                             HttpServletResponse response) {
        JsonData jsonData = productOrderService.confirmOrder(confirmOrderRequest);
        if (jsonData.getCode() != 0) {
            log.error("[创建订单失败] {}", jsonData);
        }
        writeData(response, jsonData);
    }

    @PostMapping("/repay")
    public void repayOrder(@RequestBody RepayOrderRequest repayOrderRequest,
                           HttpServletResponse response) {
        JsonData jsonData = productOrderService.repayOrder(repayOrderRequest);
        if (jsonData.getCode() != 0) {
            log.error("[重新支付订单失败] {}", jsonData);
        }
        writeData(response, jsonData);
    }

    /**
     * 查询订单状态，此接口没有登录拦截，可以增加一个密钥进行RPC通信
     *
     * @param outTradeNo 订单号
     * @return JsonData
     */
    @GetMapping("query_state")
    public JsonData queryProductOrderState(@RequestParam("out_trade_no") String outTradeNo) {
        String state = productOrderService.queryProductOrderState(outTradeNo);
        return StringUtils.isBlank(state) ? JsonData.buildResult(BizCodeEnum.ORDER_CONFIRM_NOT_EXIST) : JsonData.buildResult(BizCodeEnum.SEARCH_SUCCESS, state);
    }

    @GetMapping("page_product_order")
    public JsonData pageProductOrder(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "8") int size,
                                     @RequestParam(value = "query_type", defaultValue = "ALL") String queryType) {
        Map<String, Object> pageMap = productOrderService.pageProductActivity(page, size, queryType);
        return JsonData.buildResult(BizCodeEnum.SEARCH_SUCCESS, pageMap);
    }

    @GetMapping("get_order_token")
    public JsonData getOrderToken() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String key = String.format(CacheKey.SUBMIT_ORDER_TOKEN_KEY, loginUser.getId());
        String token = CommonUtils.getRandomStr(32);
        redisTemplate.opsForValue().set(key, token, TimeConstant.EXPIRATION_TIME_MINUTE, TimeUnit.MINUTES);
        return JsonData.buildResult(BizCodeEnum.SEARCH_SUCCESS, token);
    }

    private void writeData(HttpServletResponse response, JsonData jsonData) {
        response.setContentType("text/html;charset=UTF8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonData.getData().toString());
            writer.flush();
        } catch (Exception e) {
            log.error("[写出HTML异常] {}", e);
        }
    }

}
