package cn.skyln.web.controller;

import cn.skyln.config.AlipayConfig;
import cn.skyln.enums.order.ProductOrderPayTypeEnum;
import cn.skyln.util.JsonData;
import cn.skyln.web.service.ProductOrderService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: lamella
 * @Date: 2022/10/01/17:53
 * @Description:
 */
@Controller
@RequestMapping("/api/v1/callback/order/")
@Slf4j
public class CallbackController {

    @Autowired
    private ProductOrderService productOrderService;


    @PostMapping("alipay")
    public String alipayCallback(HttpServletRequest request, HttpServletResponse response) {
        // 将异步通知中收到的所有参数存储到map中
        Map<String, String> paramsMap = convertRequestParamsToMap(request);
        log.info("支付宝回调通知结果：{}", paramsMap);
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.ALIPAY_PUB_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);
            if(signVerified){
                JsonData jsonData = productOrderService.handlerOrderCallbackMsg(ProductOrderPayTypeEnum.ALIPAY,paramsMap);
                if(jsonData.getCode() == 0){
                    // 通知结果确认成功，不然会一直通知，八次都没返回success就认为交易失败
                    return "success";
                }
            }
        } catch (AlipayApiException e) {
            log.error("支付宝回调验证签名失败：参数：{}，异常：{}", paramsMap, e);
        }
        return "failure";
    }

    /**
     * 将request中的参数转换成Map
     *
     * @param request
     * @return
     */
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> paramsMap = new HashMap<>(16);
        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();
        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int size = values.length;
            if (size == 1) {
                paramsMap.put(name, values[0]);
            } else {
                paramsMap.put(name, "");
            }
        }
        System.out.println(paramsMap);
        return paramsMap;
    }
}
