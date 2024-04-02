package cn.skyln.util;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SecurityUtil {

    /**
     * MD5加密字符串
     *
     * @param oldStr 待加密字符串
     * @return 加密后字符串
     */
    public static String MD5(@NonNull String oldStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(oldStr.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }

            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成加密用的盐
     *
     * @param secretRefer 盐的参考内容
     * @return 盐
     */
    public static String generateSaltSecret(@Nullable String secretRefer) {
        String result = "$1$";
        if (StringUtils.isBlank(secretRefer)) {
            return result + CommonUtils.getRandomStr(8);
        }
        String md5 = MD5(secretRefer);
        String substring = StringUtils.isBlank(md5) ? CommonUtils.getRandomStr(4) : md5.substring(0, 4);
        String randomStr = CommonUtils.getRandomStr(4);
        return result + substring + randomStr;
    }

    /**
     * 使用一次性盐进行md5加密
     * <p>
     * 注意：此方法无法进行后续校验
     *
     * @param oldStr 待加密字符串
     * @return 加密后的字符串
     */
    public static String MD5WithSecret(@NonNull String oldStr) {
        return MD5WithSecret(oldStr, generateSaltSecret(null));
    }

    /**
     * 使用盐进行md5加密
     *
     * @param oldStr 待加密字符串
     * @param secret 盐
     * @return 加密后的字符串
     */
    public static String MD5WithSecret(@NonNull String oldStr, @NonNull String secret) {
        return MD5WithSecret(oldStr.getBytes(StandardCharsets.UTF_8), secret);
    }

    /**
     * 对byte数组进行md5加密
     * <p>
     * 注意：此方法无法进行后续校验
     *
     * @param bytes byte数组
     * @return 加密后的字符串
     */
    public static String MD5WithSecret(@NonNull byte[] bytes) {
        return Md5Crypt.md5Crypt(bytes, generateSaltSecret(null));
    }

    /**
     * 对byte数组进行md5加密
     *
     * @param bytes  byte数组
     * @param secret 盐
     * @return 加密后的字符串
     */
    public static String MD5WithSecret(@NonNull byte[] bytes, @NonNull String secret) {
        return Md5Crypt.md5Crypt(bytes, secret);
    }
}
