package cn.skyln.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lamella
 * @Date: 2022/09/20/20:43
 * @Description:
 */
@Configuration
@Data
public class SnowFlakeRabbitMQConfig {

    /**
     * 交换机
     */
    private String eventExchange = "id.event.exchange";
    /**
     * 第一个队列延迟队列，
     */
    private String idCloseDelayQueue = "id.release.delay.queue";
    /**
     * 第一个队列的路由key
     * 进入队列的路由key
     */
    private String idCloseDelayRoutingKey = "id.release.delay.routing.key";
    /**
     * 第二个队列，被监听恢复库存的队列
     */
    private String idCloseQueue = "id.release.queue";
    /**
     * 第二个队列的路由key
     * <p>
     * 即进入死信队列的路由key
     */
    private String idCloseRoutingKey = "id.release.routing.key";
    /**
     * 过期时间
     */
    private Integer ttl = 1000;

    /**
     * 消息转换器
     */
    @Bean(name = "snow_flake_messageConverter")
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 创建交换机 Topic类型，也可以用Direct路由
     * 一般一个微服务一个交换机
     */
    @Bean(name = "snow_flake_couponEventExchange")
    public Exchange couponEventExchange() {
        return new TopicExchange(eventExchange, true, false);
    }

    /**
     * 创建延迟队列
     */
    @Bean(name = "snow_flake_idCloseDelayQueue")
    public Queue idCloseDelayQueue() {
        Map<String, Object> args = new HashMap<>(3);
        args.put("x-message-ttl", ttl);
        args.put("x-dead-letter-routing-key", idCloseRoutingKey);
        args.put("x-dead-letter-exchange", eventExchange);
        return new Queue(idCloseDelayQueue, true, false, false, args);
    }

    /**
     * 死信队列，普通队列，用于被监听
     */
    @Bean(name = "snow_flake_idCloseQueue")
    public Queue idCloseQueue() {
        return new Queue(idCloseQueue, true, false, false);
    }

    /**
     * 死信队列绑定关系建立
     */
    @Bean(name = "snow_flake_idCloseBinding")
    public Binding idCloseBinding() {
        return new Binding(idCloseQueue, Binding.DestinationType.QUEUE, eventExchange, idCloseRoutingKey, null);
    }

    /**
     * 延迟队列绑定关系建立
     */
    @Bean(name = "snow_flake_idCloseDelayBinding")
    public Binding idCloseDelayBinding() {
        return new Binding(idCloseDelayQueue, Binding.DestinationType.QUEUE, eventExchange, idCloseDelayRoutingKey, null);
    }
}
