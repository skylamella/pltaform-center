package cn.skyln.constant;

/**
 * @author lamella
 * @since 2022/11/27/0:01
 */
public class CacheKey {
    /**
     * 注册验证码key，%s是占位符，第一个是类型，第二个是接收号码
     */
    public static final String CHECK_CODE_KEY = "%s:code:%s:%s";

    /**
     * 分部署锁key，%s是占位符，第一个是业务类型，第二个是内容ID
     */
    public static final String DISTRIBUTED_LOCK_KEY = "lock:%s:%s";

    /**
     * 购物车key，%s是占位符，是用户ID
     */
    public static final String CART_KEY = "cart:%s";

    /**
     * MQ缓存key，%s是占位符，第一个是业务类型，第二个是内容ID
     */
    public static final String MQ_KEY = "mq:%s:%s";

    /**
     * 提交表单的token key，%s是占位符，第一个是用户ID
     */
    public static final String SUBMIT_ORDER_TOKEN_KEY = "order:submit:%s";
}
