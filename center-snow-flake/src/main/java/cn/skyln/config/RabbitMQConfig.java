package cn.skyln.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
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
public class RabbitMQConfig {

    /**
     * 交换机
     */
    @Value("${mqconfig.id_event_exchange}")
    private String eventExchange;
    /**
     * 第一个队列延迟队列，
     */
    @Value("${mqconfig.id_close_delay_queue}")
    private String idCloseDelayQueue;
    /**
     * 第一个队列的路由key
     * 进入队列的路由key
     */
    @Value("${mqconfig.id_close_delay_routing_key}")
    private String idCloseDelayRoutingKey;
    /**
     * 第二个队列，被监听恢复库存的队列
     */
    @Value("${mqconfig.id_close_queue}")
    private String idCloseQueue;
    /**
     * 第二个队列的路由key
     * <p>
     * 即进入死信队列的路由key
     */
    @Value("${mqconfig.id_close_routing_key}")
    private String idCloseRoutingKey;
    /**
     * 过期时间
     */
    @Value("${mqconfig.ttl}")
    private Integer ttl;

    /**
     * 消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 创建交换机 Topic类型，也可以用Direct路由
     * 一般一个微服务一个交换机
     */
    @Bean
    public Exchange couponEventExchange() {
        return new TopicExchange(eventExchange, true, false);
    }

    /**
     * 创建延迟队列
     */
    @Bean
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
    @Bean
    public Queue idCloseQueue() {
        return new Queue(idCloseQueue, true, false, false);
    }

    /**
     * 死信队列绑定关系建立
     */
    @Bean
    public Binding idCloseBinding() {
        return new Binding(idCloseQueue, Binding.DestinationType.QUEUE, eventExchange, idCloseRoutingKey, null);
    }

    /**
     * 延迟队列绑定关系建立
     */
    @Bean
    public Binding idCloseDelayBinding() {
        return new Binding(idCloseDelayQueue, Binding.DestinationType.QUEUE, eventExchange, idCloseDelayRoutingKey, null);
    }
}
