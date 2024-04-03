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
    @Value("${mqconfig.test_event_exchange}")
    private String eventExchange;
    /**
     * 第一个队列延迟队列，
     */
    @Value("${mqconfig.test_release_delay_queue}")
    private String testReleaseDelayQueue;
    /**
     * 第一个队列的路由key
     * 进入队列的路由key
     */
    @Value("${mqconfig.test_release_delay_routing_key}")
    private String testReleaseDelayRoutingKey;
    /**
     * 第二个队列，被监听恢复库存的队列
     */
    @Value("${mqconfig.test_release_queue}")
    private String testReleaseQueue;
    /**
     * 第二个队列的路由key
     * <p>
     * 即进入死信队列的路由key
     */
    @Value("${mqconfig.test_release_routing_key}")
    private String testReleaseRoutingKey;
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
    public Exchange testEventExchange() {
        return new TopicExchange(eventExchange, true, false);
    }

    /**
     * 创建延迟队列
     */
    @Bean
    public Queue testReleaseDelayQueue() {
        Map<String, Object> args = new HashMap<>(3);
        args.put("x-message-ttl", ttl);
        args.put("x-dead-letter-routing-key", testReleaseRoutingKey);
        args.put("x-dead-letter-exchange", eventExchange);
        return new Queue(testReleaseDelayQueue, true, false, false, args);
    }

    /**
     * 死信队列，普通队列，用于被监听
     */
    @Bean
    public Queue testReleaseQueue() {
        return new Queue(testReleaseQueue, true, false, false);
    }

    /**
     * 死信队列绑定关系建立
     */
    @Bean
    public Binding testReleaseBinding() {
        return new Binding(testReleaseQueue, Binding.DestinationType.QUEUE, eventExchange, testReleaseRoutingKey, null);
    }

    /**
     * 延迟队列绑定关系建立
     */
    @Bean
    public Binding testReleaseDelayBinding() {
        return new Binding(testReleaseDelayQueue, Binding.DestinationType.QUEUE, eventExchange, testReleaseDelayRoutingKey, null);
    }
}
