package cn.skyln.mq;

import cn.skyln.service.SnowFlakeIdService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: lamella
 * @Date: 2022/09/24/23:11
 * @Description:
 */
@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.id_close_queue}")
public class SnowFlakeIdMQListener {
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SnowFlakeIdService snowFlakeIdService;

    @RabbitHandler
    public void delayGenerateSnowFlakeIds(Message message, Channel channel) throws IOException {
        String lockKey = "SNOW_FLAKE_ID_GENERATE";
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        log.info("监听到消息：发号器生成ID消息");
        long msgTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("发号器生成ID-分布式锁加锁成功:{}", Thread.currentThread().getId());
            if (snowFlakeIdService.delayGenerateIds()) {
                // 确认消息消费成功
                channel.basicAck(msgTag, false);
            } else {
                channel.basicReject(msgTag, true);
            }
        } catch (Exception e) {
            log.error("发号器生成ID-记录异常：{}", e);
            channel.basicReject(msgTag, true);
            lock.unlock();
        } finally {
            lock.unlock();
            log.info("发号器生成ID-分布式锁解锁成功:{}", Thread.currentThread().getId());
        }
    }
}
