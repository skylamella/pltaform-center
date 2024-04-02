package cn.skyln.servce;

/**
 * @author lamella
 * @description SnowFlakeIdService TODO
 * @since 2024-04-02 22:23
 */
public interface SnowFlakeIdService {

    String getNextId(String serviceName);

    void delayGenerateIds();
}
