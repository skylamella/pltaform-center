package cn.skyln.mq;

import cn.skyln.constant.CacheKey;
import cn.skyln.enums.MQChannelStateEnum;
import cn.skyln.model.CartMessage;
import cn.skyln.util.CheckUtil;
import cn.skyln.web.service.CartService;
import cn.skyln.web.service.MqErrorLogService;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: lamella
 * @Date: 2022/09/23/22:20
 * @Description:
 */
@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.cart_release_queue}")
public class CartMQListener {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CartService cartService;

    @Resource(name = "serviceDbTemplate")
    private RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private MqErrorLogService mqErrorLogService;

    @RabbitHandler
    public void cleanCartRecord(CartMessage cartMessage, Message message, Channel channel) throws IOException {
        String lockKey = String.format(CacheKey.DISTRIBUTED_LOCK_KEY, MQChannelStateEnum.CLEAN_CART_RECORD.name(), cartMessage.getOutTradeNo() + cartMessage.getProductId());
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        log.info("监听到消息：cleanCartRecord消息内容：{}", cartMessage);
        long msgTag = message.getMessageProperties().getDeliveryTag();
        int retryNums = 1;
        String mqKey = String.format(CacheKey.MQ_KEY, MQChannelStateEnum.MQ_CLEAN_CART_RECORD.name(), cartMessage.getOutTradeNo() + ":" + cartMessage.getProductId());
        try {
            log.info("{}-分布式锁加锁成功:{}", MQChannelStateEnum.CLEAN_CART_RECORD.getMsg(), Thread.currentThread().getId());
            if (cartService.cleanCartRecord(cartMessage)) {
                // 确认消息消费成功
                channel.basicAck(msgTag, false);
            } else {
                retryNums = CheckUtil.checkMQRetryNums(redisTemplate,
                        log,
                        mqKey,
                        cartMessage,
                        msgTag,
                        channel,
                        MQChannelStateEnum.CLEAN_CART_RECORD.getMsg());
            }
        } catch (Exception e) {
            log.error("{}-记录异常：{}，msg：{}", MQChannelStateEnum.CLEAN_CART_RECORD.getMsg(), e, cartMessage);
            retryNums = Math.max(retryNums, CheckUtil.checkMQRetryNums(redisTemplate,
                    log,
                    mqKey,
                    cartMessage,
                    msgTag,
                    channel,
                    MQChannelStateEnum.CLEAN_CART_RECORD.getMsg()));
            lock.unlock();
        } finally {
            if (retryNums >= 5) {
                // 持续消费失败则插入数据库
                mqErrorLogService.insertMqErrorLog(cartMessage.getOutTradeNo());
            }
            lock.unlock();
            log.info("{}-分布式锁解锁成功:{}", MQChannelStateEnum.CLEAN_CART_RECORD.getMsg(), Thread.currentThread().getId());
        }
    }
}
