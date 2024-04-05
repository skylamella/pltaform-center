package cn.skyln.web.service.impl;

import cn.skyln.component.factories.SendCodeFactory;
import cn.skyln.constant.TimeConstant;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.util.CommonUtils;
import cn.skyln.util.JsonData;
import cn.skyln.web.service.NotifyService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author lamella
 * @description NotifyServiceImpl TODO
 * @since 2024-04-03 18:16
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private SendCodeFactory sendCodeFactory;

    @Resource(name = "cacheDbTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public JsonData sendCode(String enumName, String to) {
        String cacheKey = sendCodeFactory.generateCacheKey(enumName, to, "center-account");
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        // 如果redis中存在缓存，则判断是否为60秒内重复发送
        if (StringUtils.isNotBlank(cacheValue)) {
            long ttl = Long.parseLong(cacheValue.split("_")[1]);
            // 当前时间戳减去验证码发送时间戳，如果小于60秒，则判定为重复发送
            long opsRepeatSendTimeStamp = CommonUtils.getCurrentTimeStamp() - ttl;
            if (opsRepeatSendTimeStamp < (60 * 1000)) {
                log.error("重复发送验证码，时间间隔：{}秒", opsRepeatSendTimeStamp / 1000);
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }
        String code = CommonUtils.getRandomSMSCode();
        String value = code + "_" + CommonUtils.getCurrentTimeStamp();
        redisTemplate.opsForValue().set(cacheKey, value, TimeConstant.CAPTCHA_CODE_EXPIRED, TimeUnit.MINUTES);
        sendCodeFactory.sendCode(enumName, to, code, "center-account");
        log.info("{}的验证码发送成功！", to);
        return JsonData.buildResult(BizCodeEnum.SEND_CODE_SUCCESS, to);
    }

    @Override
    public boolean checkCode(String enumName, String to, String code) {
        String cacheKey = sendCodeFactory.generateCacheKey(enumName, to, "center-account");
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotBlank(cacheValue)) {
            if (StringUtils.equals(cacheValue.split("_")[0], code)) {
                redisTemplate.delete(cacheKey);
                return true;
            }
        }
        return false;
    }
}
