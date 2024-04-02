package cn.skyln.config;

import cn.skyln.service.SnowFlakeIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitStartRun implements InitializingBean {

    @Autowired
    private SnowFlakeIdService snowFlakeIdService;

    @Override
    public void afterPropertiesSet() throws Exception {
        snowFlakeIdService.delayGenerateIds();
    }
}
