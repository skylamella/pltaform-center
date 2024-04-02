package cn.skyln.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lamella
 * @since 2022/11/27/19:18
 */
@Data
@ConfigurationProperties(prefix = "sms")
@Configuration
public class SMSConfig {
    private String appCode;
    private String smsSignId;
    private String host;
    private String path;
}
