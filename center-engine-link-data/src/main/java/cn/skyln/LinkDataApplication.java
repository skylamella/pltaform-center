package cn.skyln;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lamella
 * @description LinkDataApplication TODO
 * @since 2024-04-03 22:15
 */
@SpringBootApplication
@MapperScan("cn.skyln.web.dao.mapper")
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAsync
public class LinkDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinkDataApplication.class, args);
    }
}

