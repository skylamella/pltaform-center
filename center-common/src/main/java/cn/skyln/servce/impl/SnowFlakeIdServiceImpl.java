package cn.skyln.servce.impl;

import cn.skyln.config.SnowFlakeRabbitMQConfig;
import cn.skyln.servce.SnowFlakeIdService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author lamella
 * @description SnowFlakeIdServiceImpl TODO
 * @since 2024-04-02 22:24
 */
@Service
public class SnowFlakeIdServiceImpl implements SnowFlakeIdService {

    @Resource(name = "idWorkerTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SnowFlakeRabbitMQConfig rabbitMQConfig;

    @Override
    public String getNextId(String serviceName) {
        delayGenerateIds();
        return serviceName + redisTemplate.opsForList().leftPop("SNOW_FLAKE_ID");
    }

    @Override
    public void delayGenerateIds() {
        if (Boolean.TRUE.equals(redisTemplate.hasKey("SNOW_FLAKE_ID"))) {
            Long idSize = redisTemplate.opsForList().size("SNOW_FLAKE_ID");
            if (Objects.nonNull(idSize)) {
                if (idSize < 100000L) {
                    sendGenerateIdMessage();
                }
            } else {
                sendGenerateIdMessage();
            }
        } else {
            sendGenerateIdMessage();
        }
    }

    private void sendGenerateIdMessage() {
        rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),
                rabbitMQConfig.getIdCloseDelayRoutingKey());
    }
}
