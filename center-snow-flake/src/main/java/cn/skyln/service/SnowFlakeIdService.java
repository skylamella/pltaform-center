package cn.skyln.service;

public interface SnowFlakeIdService {

    /**
     * 监听到生成ID的MQ消息
     *
     * @return ID生成状态
     */
    boolean delayGenerateIds();

}
