package cn.skyln.component.factories;

import cn.skyln.component.factories.context.SendCodeStrategyContext;
import cn.skyln.component.factories.strategy.impl.MailComponent;
import cn.skyln.component.factories.strategy.impl.SMSComponent;
import cn.skyln.constant.CacheKey;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.exception.BizException;
import cn.skyln.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.util.PhoneUtil.isPhone;
import static cn.skyln.constant.CacheValue.VALUE_COMMON_FORMAT;
import static cn.skyln.constant.TimeConstant.CAPTCHA_CODE_EXPIRED;
import static cn.skyln.util.CheckUtil.isEmail;

@Component
@Slf4j
public class SendCodeFactory {

    @Autowired
    private SMSComponent smsComponent;

    @Autowired
    private MailComponent mailComponent;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String key;

    public void sendCode(String enumName, String to, String code, String serviceName) {
        SendCodeStrategyContext sendCodeStrategyContext = getSendCodeStrategyContext(enumName, to, serviceName);
        if (Objects.nonNull(sendCodeStrategyContext)) {
            long currentTimeStamp = CommonUtils.getCurrentTimeStamp();
            if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                String cacheValue = redisTemplate.opsForValue().get(key);
                if (StringUtils.isNotBlank(cacheValue)) {
                    // 当前时间戳减去验证码发送时间戳，如果小于60秒，则判定为重复发送
                    if ((currentTimeStamp - Long.parseLong(cacheValue.split("_")[2])) < (60 * 1000)) {
                        throw new BizException(BizCodeEnum.CODE_LIMITED);
                    }
                    redisTemplate.delete(key);
                }
            }
            sendCodeStrategyContext.sendCode(enumName, to, code);
            String value = String.format(VALUE_COMMON_FORMAT, code, 3, currentTimeStamp);
            redisTemplate.opsForValue().set(key, value, CAPTCHA_CODE_EXPIRED, TimeUnit.MINUTES);
        }
        throw new BizException(BizCodeEnum.CODE_TO_ERROR);
    }

    public String generateCacheKey(String enumName, String to, String serviceName) {
        if (isPhone(to)) {
            return String.format(CacheKey.CHECK_CODE_KEY, serviceName, "sms-" + enumName, to);
        } else if (isEmail(to)) {
            return String.format(CacheKey.CHECK_CODE_KEY, serviceName, "mail-" + enumName, to);
        }
        return null;
    }

    private SendCodeStrategyContext getSendCodeStrategyContext(String enumName, String to, String serviceName) {
        key = generateCacheKey(enumName, to, serviceName);
        if (isPhone(to)) {
            return new SendCodeStrategyContext(smsComponent);
        } else if (isEmail(to)) {
            return new SendCodeStrategyContext(mailComponent);
        }
        return null;
    }
}
