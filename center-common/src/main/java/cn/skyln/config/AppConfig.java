package cn.skyln.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @author lamella
 * @description AppConfig TODO
 * @since 2024-04-07 21:52
 */
@Configuration
@Data
@Slf4j
public class AppConfig {

    /**
     * feign调用丢失token解决方式，新增拦截器
     *
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(attributes)) {
                HttpServletRequest request = attributes.getRequest();
                log.info(request.getHeaderNames().toString());
                template.header("token", request.getHeader("token"));
            } else {
                log.warn("requestInterceptor获取Header空指针异常");
            }
        };
    }

}
