package cn.skyln;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IdWorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdWorkerApplication.class, args);
    }
}
