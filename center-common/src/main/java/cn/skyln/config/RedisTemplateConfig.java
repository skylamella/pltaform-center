package cn.skyln.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;

@Configuration
public class RedisTemplateConfig {

    @Value("${redis.idWorkerDb}")
    private int idWorkerDb;

    @Value("${redis.serviceDb}")
    private int serviceDb;

    @Value("${redis.cacheDb}")
    private int cacheDb;

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.password}")
    private String password;
    private static RedissonClient redisson = null;

    /**
     * 雪花ID算法专用
     *
     * @return
     */
    @Bean("idWorkerTemplate")
    public RedisTemplate<String, String> idWorkerTemplate() {
        return getRedisTemplate(idWorkerDb);
    }

    /**
     * 各微服务主要使用的缓存数据库
     *
     * @param <T>
     * @return
     */
    @Bean("serviceDbTemplate")
    public <T> RedisTemplate<String, T> serviceDbTemplate() {
        return getRedisTemplate(serviceDb);
    }

    /**
     * 缓存诸如token、验证码等信息
     *
     * @param <T>
     * @return
     */
    @Bean("cacheDbTemplate")
    public <T> RedisTemplate<String, T> cacheDbTemplate() {
        return getRedisTemplate(cacheDb);
    }

    @Bean
    public synchronized RedissonClient redissonClient() {
        if (Objects.isNull(redisson)) {
            Config config = new Config();
            //单机模式
            config.useSingleServer()
                    .setPassword(password)
                    .setAddress("redis://" + host + ":" + port);
            //集群模式
//            config.useClusterServers()
//                    .setScanInterval(2000)
//                    .addNodeAddress("redis://10.0.29.30:6379", "redis://10.0.29.95:6379");
            redisson = Redisson.create(config);
        }
        return redisson;
    }

    private <T> RedisTemplate<String, T> getRedisTemplate(int databaseNum) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(databaseNum);
        configuration.setHostName(host);
        configuration.setPassword(password);
        configuration.setPort(port);
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(new LettuceConnectionFactory(configuration));

        //配置序列化规则
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);

        //设置key-value序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);

        //设置hash key-value序列化规则
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        return redisTemplate;
    }
}
