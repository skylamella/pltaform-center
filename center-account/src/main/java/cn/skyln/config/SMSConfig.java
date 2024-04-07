package cn.skyln.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author lamella
 * @since 2022/11/27/19:18
 */
@Data
@Configuration
public class SMSConfig {
    @Value("${sms.app-code}")
    private String appCode;
    @Value("${sms.sms-sign-id}")
    private String smsSignId;
    @Value("${sms.host}")
    private String host;
    @Value("${sms.path}")
    private String path;
}
