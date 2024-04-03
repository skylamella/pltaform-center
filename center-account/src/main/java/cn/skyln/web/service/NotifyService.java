package cn.skyln.web.service;

import cn.skyln.util.JsonData;

/**
 * @author lamella
 * @description NotifyService TODO
 * @since 2024-04-03 18:16
 */
public interface NotifyService {

    JsonData sendCode(String enumName, String to);

    boolean checkCode(String enumName, String to, String code);
}
