package cn.skyln.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;

/**
 * @Author: lamella
 * @Date: 2022/09/10/11:23
 * @Description:
 */
@Configuration
@Data
@Slf4j
public class AppConfig {
    private static RedissonClient redisson = null;
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private Integer redisPort;
    @Value("${redis.password}")
    private String redisPwd;
    @Value("${redis.idWorkerDb}")
    private Integer idWorkerDb;
    @Value("${redis.serviceDb}")
    private Integer serviceDb;

    /**
     * 配置分布式锁客户端
     *
     * @return
     */
    @Bean
    public synchronized RedissonClient redissonClient() {
        if (Objects.isNull(redisson)) {
            Config config = new Config();
            //单机模式
            config.useSingleServer()
                    .setDatabase(serviceDb)
                    .setPassword(redisPwd)
                    .setAddress("redis://" + redisHost + ":" + redisPort);
            //集群模式
//            config.useClusterServers()
//                    .setScanInterval(2000)
//                    .addNodeAddress("redis://10.0.29.30:6379", "redis://10.0.29.95:6379");
            redisson = Redisson.create(config);
        }
        return redisson;
    }


    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(idWorkerDb);
        configuration.setHostName(redisHost);
        configuration.setPassword(redisPwd);
        configuration.setPort(redisPort);
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(new LettuceConnectionFactory(configuration));

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 设置key和value的序列化规则
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(serializer);
        // 设置hashKey和hashValue的序列化规则
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        // 设置支持事物
        //redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
