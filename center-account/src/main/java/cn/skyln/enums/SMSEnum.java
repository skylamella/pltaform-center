package cn.skyln.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lamella
 * @since 2022/11/27/0:01
 */
public enum SMSEnum {

    /**
     * 短信通用验证码模板
     */
    COMMON("**code**:%s,**minute**:%s", "908e94ccf08b4476ba6c876d13f084ad"),
    /**
     * 注册短信验证码模板
     */
    REGISTRATION("**code**:%s,**minute**:%s", "a09602b817fd47e59e7c6e603d3f088d"),
    /**
     * 修改密码短信验证码模板
     */
    CHANGE_PWD("**code**:%s,**minute**:%s", "29833afb9ae94f21a3f66af908d54627"),
    /**
     * 身份验证短信验证码模板
     */
    AUTHENTICATION("**code**:%s", "d6d95d8fb03c4246b944abcc1ea7efd8"),
    /**
     * 登录确认短信验证码模板
     */
    LOGIN("**code**:%s,**minute**:%s", "02551a4313154fe4805794ca069d70bf"),
    /**
     * 登录异常短信验证码模板
     */
    LOGIN_ERROR("**code**:%s,**minute**:%s", "81e8a442ea904694a37d2cec6ea6f2bc"),
    /**
     * 信息变更短信验证码模板
     */
    CHANGE_INFO("**code**:%s,**minute**:%s", "ea66d14c664649a69a19a6b47ba028db");

    @Getter
    private final String templateId;

    @Getter
    private final String param;

    private SMSEnum(String param, String templateId) {
        this.param = param;
        this.templateId = templateId;
    }

    public static SMSEnum getSMSEnumByName(String name) {
        List<SMSEnum> collect = Arrays.stream(SMSEnum.values()).filter(obj -> StringUtils.equalsIgnoreCase(obj.name(), name))
                .collect(Collectors.toList());
        return collect.size() != 1 ? null : collect.get(0);
    }

    /**
     * 格式化返回的param
     * <p>
     * 当param中不存在所需的过期时间时，将只格式化进去验证码
     *
     * @param code    验证码
     * @param minutes 过期时间
     * @return 格式化返回的param
     */
    public String getParam(String code, String minutes) {
        return String.format(this.param, code, minutes);
    }
}
