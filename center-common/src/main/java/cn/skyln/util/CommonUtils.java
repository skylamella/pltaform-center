package cn.skyln.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.*;

import static cn.skyln.constant.CharConstant.ALL_CHAR_NUM;
import static cn.skyln.constant.CharConstant.NUM_CHAR;

/**
 * @Author: lamella
 * @Date: 2022/08/20/16:02
 * @Description:
 */
@Slf4j
public class CommonUtils {

    /**
     * 获取完整URL
     *
     * @param request HttpServletRequest
     * @return 完整URL
     */
    public static String getFullUrl(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (!StringUtils.isEmpty(queryString)) {
            requestURL = requestURL + "?" + queryString;
        }
        return requestURL;
    }

    /**
     * 获取全部请求头
     *
     * @param request HttpServletRequest
     * @return 请求头
     */
    public static Map<String, String> getAllRequestHeader(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> map = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            //根据名称获取请求头的值
            String value = request.getHeader(key);
            map.put(key.toLowerCase(Locale.ENGLISH), value);
        }

        return map;
    }

    /**
     * 获取一个随机整数
     *
     * @param maxNum 随机整数边界
     * @return 随机整数
     */
    public static int getRandomNum(int maxNum) {
        if (maxNum == 1) {
            return 0;
        } else {
            Random random = new Random();
            int temp = random.nextInt(maxNum);
            if (temp > 0 && temp == maxNum) {
                return temp - 1;
            } else {
                return temp;
            }
        }
    }

    /**
     * 获取访问用户的IP地址
     *
     * @param request HttpServletRequest
     * @return 访问用户的IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            log.warn("获取IP地址异常:{}", e);
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * 生成随机字符串
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getRandomStr(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_CHAR_NUM.charAt(getRandomNum(ALL_CHAR_NUM.length() - 1)));
        }
        return sb.toString();
    }

    public static String getRandomSMSCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(NUM_CHAR.charAt(getRandomNum(NUM_CHAR.length() - 1)));
        }
        return sb.toString();
    }

    public static long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 向前台发送json字符串
     *
     * @param response HttpServletResponse
     * @param obj      转化后的json字符串
     */
    public static void renderJson(HttpServletResponse response, Object obj) {
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JsonUtil.obj2Json(obj));
            response.flushBuffer();
        } catch (Exception e) {
            log.error("响应json数据给前端异常:{}", e);
        }
    }

    /**
     * 生成uuid
     *
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Map<String, Object> getReturnPageMap(long total, long pages, List<Object> collect) {
        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record", total);
        pageMap.put("total_page", pages);
        pageMap.put("current_data", collect);
        return pageMap;
    }

    public static Object beanProcess(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
