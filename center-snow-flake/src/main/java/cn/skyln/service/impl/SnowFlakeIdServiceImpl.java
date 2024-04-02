package cn.skyln.service.impl;

import cn.skyln.service.SnowFlakeIdService;
import cn.skyln.util.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class SnowFlakeIdServiceImpl implements SnowFlakeIdService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean delayGenerateIds() {
        if (Boolean.TRUE.equals(redisTemplate.hasKey("SNOW_FLAKE_ID"))) {
            Long idSize = redisTemplate.opsForList().size("SNOW_FLAKE_ID");
            if (Objects.nonNull(idSize)) {
                if (idSize < 100000L) {
                    return sendGenerateIdMessage();
                } else {
                    return true;
                }
            } else {
                return sendGenerateIdMessage();
            }
        } else {
            return sendGenerateIdMessage();
        }
    }

    private boolean sendGenerateIdMessage() {
        try {
            Sequence sequence = new Sequence();
            Set<String> set = new HashSet<>();
            for (int i = 0; i < 1000000; i++) {
                set.add(String.valueOf(sequence.nextId()));
            }
            redisTemplate.opsForList().leftPushAll("SNOW_FLAKE_ID", set);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
