package cn.skyln.component.factories.strategy.impl;

import cn.skyln.component.factories.strategy.SendCodeStrategy;
import cn.skyln.config.SMSConfig;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.enums.SMSEnum;
import cn.skyln.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.skyln.constant.TimeConstant.CAPTCHA_CODE_EXPIRED;

/**
 * @author lamella
 * @since 2022/11/27/19:51
 */
@Service
@Slf4j
public class SMSComponent implements SendCodeStrategy {
    @Autowired
    private SMSConfig smsConfig;

    @Autowired
    private RestTemplate restTemplate;

    private static String buildUrl(String host, String path, Map<String, String> query) {
        StringBuilder url = new StringBuilder();
        url.append(host).append(path);
        if (Objects.nonNull(query) && !query.isEmpty()) {
            StringBuilder queryStr = new StringBuilder();
            query.keySet().forEach(key -> {
                if (!queryStr.isEmpty()) {
                    queryStr.append("&");
                }
                queryStr.append(key).append("=").append(query.get(key));
            });
            if (!queryStr.isEmpty()) {
                url.append("?").append(queryStr);
            }
        }
        return url.toString();
    }

    @Override
    public void sendCode(String enumName, String mobile, String SMSCode) {
        SMSEnum smsEnum = SMSEnum.getSMSEnumByName(enumName);
        if (Objects.isNull(smsEnum)) {
            throw new BizException(BizCodeEnum.CODE_TYPE_ERROR);
        }
        Map<String, String> query = new HashMap<>();
        query.put("mobile", mobile);
        query.put("param", smsEnum.getParam(SMSCode, String.valueOf(CAPTCHA_CODE_EXPIRED)));
        query.put("smsSignId", smsConfig.getSmsSignId());
        query.put("templateId", smsEnum.getTemplateId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "APPCODE " + smsConfig.getAppCode());
        String url = buildUrl(smsConfig.getHost(), smsConfig.getPath(), query);
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if (HttpStatus.OK == response.getStatusCode()) log.info("发送短信成功,响应信息:{}", response.getBody());
        else log.error("发送短信失败,响应信息:{}", response.getBody());
    }
}
