package cn.skyln;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lamella
 * @description TestDataApplication TODO
 * @since 2024-02-10 15:47
 */

@SpringBootApplication
@MapperScan("cn.skyln.web.dao.mapper")
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAsync
public class TestDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestDataApplication.class, args);
    }
}
