package cn.skyln.web.controller;

import cn.skyln.enums.BizCodeEnum;
import cn.skyln.exception.BizException;
import cn.skyln.util.CommonUtils;
import cn.skyln.util.JsonData;
import cn.skyln.util.SecurityUtil;
import cn.skyln.web.service.NotifyService;
import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import static cn.skyln.constant.TimeConstant.CAPTCHA_CODE_EXPIRED;

/**
 * @author lamella
 * @description NotifyController TODO
 * @since 2024-04-03 19:10
 */
@RestController
@RequestMapping("/api/v1/notify")
@Slf4j
public class NotifyController {
    @Autowired
    @Qualifier("captchaProducer")
    private Producer captchaProducer;

    @Autowired
    private NotifyService notifyService;

    @Resource(name = "cacheDbTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String captchaText = captchaProducer.createText();
        redisTemplate.opsForValue().set(getCaptchaKey(request), captchaText, CAPTCHA_CODE_EXPIRED, TimeUnit.MINUTES);
        log.info("[图形验证码： {}]", captchaText);
        BufferedImage image = captchaProducer.createImage(captchaText);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
        } catch (Exception e) {
            throw new BizException(e.hashCode(), e.getMessage());
        }
    }

    @PostMapping("send_code")
    public JsonData sendRegisterCode(@RequestParam(value = "to") String to,
                                     @RequestParam(value = "captcha") String captcha,
                                     HttpServletRequest request) {
        String key = getCaptchaKey(request);
        String cacheCaptcha = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(cacheCaptcha) || !StringUtils.equalsIgnoreCase(captcha, cacheCaptcha)) {
            return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA_ERROR);
        } else {
            redisTemplate.delete(key);
            return notifyService.sendCode("REGISTRATION", to);
        }
    }

    /**
     * 获取缓存的key
     *
     * @param request
     * @return
     */
    private String getCaptchaKey(HttpServletRequest request) {
        return "account-service:captcha:" + SecurityUtil.MD5(request.getHeader("User-Agent") + CommonUtils.getIpAddr(request));
    }
}
