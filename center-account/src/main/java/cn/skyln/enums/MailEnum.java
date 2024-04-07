package cn.skyln.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author lamella
 * @since 2022/11/27/0:01
 */
public enum MailEnum {

    /**
     * 短信通用验证码模板
     */
    COMMON("您的验证码是\"%s\"，有效期10分钟，请勿向他人透漏验证码。", "验证码"),
    /**
     * 注册短信验证码模板
     */
    REGISTRATION("欢迎注册，您的验证码是\"%s\"，有效期10分钟，请勿向他人透漏验证码。", "注册验证码"),
    /**
     * 修改密码短信验证码模板
     */
    CHANGE_PWD("您正在修改密码，验证码是\"%s\"，有效期10分钟，请勿向他人透漏验证码。", "修改密码验证码"),
    /**
     * 身份验证短信验证码模板
     */
    AUTHENTICATION("您正在进行身份验证，验证码是\"%s\"，有效期10分钟，请勿向他人透漏验证码。", "身份验证验证码"),
    /**
     * 登录确认短信验证码模板
     */
    LOGIN("您正在进行登录确认，验证码是\"%s\"，有效期10分钟，请勿向他人透漏验证码。", "登录确认验证码"),
    /**
     * 登录异常短信验证码模板
     */
    LOGIN_ERROR("您当前登录异常，请使用验证码登录，验证码是\"%s\"，有效期10分钟，请勿向他人透漏验证码。", "登录异常验证码"),
    /**
     * 信息变更短信验证码模板
     */
    CHANGE_INFO("您正在修改个人信息，验证码是\"%s\"，有效期10分钟，请勿向他人透漏验证码。", "修改信息验证码");

    @Getter
    private final String templateId;

    @Getter
    private final String param;

    private MailEnum(String param, String templateId) {
        this.param = param;
        this.templateId = templateId;
    }

    public static MailEnum getMailEnumByName(String name) {
        List<MailEnum> collect = Arrays.stream(MailEnum.values()).filter(obj -> StringUtils.equalsIgnoreCase(obj.name(), name))
                .toList();
        return collect.size() != 1 ? null : collect.get(0);
    }

    /**
     * 格式化返回的param
     * <p>
     * 当param中不存在所需的过期时间时，将只格式化进去验证码
     *
     * @param code 验证码
     * @return 格式化返回的param
     */
    public String getParam(String code) {
        return String.format(this.param, code);
    }
}
