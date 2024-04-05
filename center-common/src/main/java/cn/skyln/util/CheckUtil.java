package cn.skyln.util;

import cn.skyln.constant.CacheKey;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.exception.BizException;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static cn.hutool.core.lang.PatternPool.EMAIL;
import static cn.hutool.core.lang.PatternPool.EMAIL_WITH_CHINESE;

/**
 * @Author: lamella
 * @Date: 2022/09/02/21:44
 * @Description:
 */
public class CheckUtil {

    /**
     * IPV4正则
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    /**
     * IPV6正则
     */
    private static final Pattern IPV6_PATTERN = Pattern.compile("^([0-9a-fA-F]{0,4}:){2,7}[0-9a-fA-F]{0,4}$");

    /**
     * 用于检查IPv4映射的IPv6地址的正则表达式
     */
    private static final Pattern IPV4_MAPPED_IPV6_PATTERN = Pattern.compile("^::ffff:((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    /**
     * @param email 邮箱号
     * @return 是否是邮箱
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return EMAIL.matcher(email).matches() || EMAIL_WITH_CHINESE.matcher(email).matches();
    }

    /**
     * 通用性MQ消费次数判定
     *
     * @param redisTemplate redisTemplate
     * @param log           sl4j日志
     * @param mqKey         mqKey
     * @param msg           msg
     * @param msgTag        msgTag
     * @param channel       channel
     * @param serviceName   serviceName
     * @return 重试次数
     */
    public static int checkMQRetryNums(RedisTemplate<String, Integer> redisTemplate, Logger log, String mqKey, Object msg, long msgTag, Channel channel, String serviceName) throws IOException {
        int retryNums = 1;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(mqKey))) {
            Integer tempNum = redisTemplate.opsForValue().get(mqKey);
            if (Objects.nonNull(tempNum)) {
                retryNums = tempNum;
                redisTemplate.delete(mqKey);
                if (retryNums < 5) {
                    redisTemplate.opsForValue().set(mqKey, ++retryNums);
                    log.error("{}-失败，第{}次重试：{}", serviceName, retryNums, msg);
                    channel.basicReject(msgTag, true);
                } else {
                    log.error("{}-失败，重试次数超过5次：{}", serviceName, msg);
                    // 重试次数超过5次，确认消息消费成功
                    channel.basicAck(msgTag, false);
                }
            }
        } else {
            log.error("{}-失败，第1次重试：{}", serviceName, msg);
            redisTemplate.opsForValue().set(mqKey, 1);
            channel.basicReject(msgTag, true);
        }
        return retryNums;
    }

    /**
     * 防重下单token校验
     *
     * @param orderToken    下单token
     * @param log           sl4j日志
     * @param redisTemplate redisTemplate
     * @param loginUserId   登录用户ID
     */
    public static void checkOrderToken(String orderToken, Logger log, RedisTemplate<String, Long> redisTemplate, String loginUserId) {
        if (StringUtils.isBlank(orderToken)) {
            log.error("用户下单令牌不存在");
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_TOKEN_NOT_EXIST);
        }
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        String key = String.format(CacheKey.SUBMIT_ORDER_TOKEN_KEY, loginUserId);
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), List.of(key), orderToken);
        assert result != null;
        if (result == 0L) {
            log.error("用户下单订单令牌不正确：{}", orderToken);
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_TOKEN_EQUAL_FAIL);
        }
    }

    public static boolean checkIp(String ip) {
        if (StringUtils.isBlank(ip)) return false;
        // 首先尝试IPv4格式
        if (IPV4_PATTERN.matcher(ip).matches() || IPV4_MAPPED_IPV6_PATTERN.matcher(ip).matches()) {
            return isValidIPv4(ip);
        }
        // 接着尝试IPv6格式
        else if (IPV6_PATTERN.matcher(ip).matches()) {
            return isValidIPv6(ip);
        }
        return false;
    }

    public static boolean isValidIPv4(String ip) {
        String[] s = ip.split("\\.");
        // 由于正则已经保证了数字范围，这里只需确保没有额外的字符
        for (String segment : s) {
            if (!segment.matches("\\d{1,3}")) {
                return false;
            }
            int value = Integer.parseInt(segment);
            if (value < 0 || value > 255) {
                return false; // 确保数值在0-255之间
            }
        }
        // 避免如0.0.0.0的特殊地址
        return !ip.equals("0.0.0.0");
    }

    public static boolean isValidIPv6(String ip) {
        int doubleColonCount = 0;
        String[] parts = ip.split(":");
        if (parts.length > 8) {
            return false; // 如果分割后的部分超过8组，则地址无效
        }
        for (String part : parts) {
            if (part.isEmpty() && doubleColonCount == 0) {
                doubleColonCount++;
                continue; // 遇到第一个双冒号，记录并继续
            } else if (part.isEmpty() && doubleColonCount == 1) {
                return false; // 如果出现第二个双冒号，则地址无效
            } else if (part.contains(".")) {
                // 检查是否为IPv4映射地址
                if (IPV4_PATTERN.matcher(part).matches()) {
                    continue;
                } else {
                    return false; // IPv6地址中不应包含点号，除非是IPv4映射地址的一部分
                }
            } else if (part.length() > 4) {
                return false; // 每个部分的长度不应超过4个字符
            }
            try {
                int value = Integer.parseInt(part, 16);
                if (value < 0 || value > 0xFFFF) {
                    return false; // 超出16位无符号整数范围
                }
            } catch (NumberFormatException e) {
                return false; // 部分无法解析为有效的十六进制数
            }
        }
        // 如果双冒号压缩形式存在，则必须正好缺少两组数字
        return doubleColonCount == 1 ? parts.length + 1 == 8 : parts.length == 8;
    }
}
