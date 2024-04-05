package cn.skyln.util;

import org.springframework.lang.Nullable;

import java.util.regex.Pattern;

import static cn.hutool.core.util.PhoneUtil.isPhone;

/**
 * @author lamella
 * @description PhoneUtil TODO
 * @since 2024-04-05 20:55
 */
public class PhoneUtil {

    /**
     * 中国移动134-0~8 / 135 / 136 / 137 / 138 / 139 / 150 / 151 / 152 / 157 / 158 / 159 / 178 / 182 / 183 / 184 / 187 / 188 / 198
     */
    private static final Pattern CHINA_MOBILE_PATTERN = Pattern.compile("^(((13[5-9])|(15[0-2,7-9])|(178)|(18[2-4,7-8])|(198))\\d{8})|(134[0-8]]\\d{7})$");
    /**
     * 中国联通130 / 131 / 132 / 155 / 156 / 166 / 175 / 176 / 185 / 186
     */
    private static final Pattern CHINA_UNICOM_PATTERN = Pattern.compile("^((13[0-2])|(15[5,6])|(166)|(1[7,8][5,6]))\\d{8}$");
    /**
     * 中国电信133 / 134-9 / 153 / 173 / 174-00~05 / 177 / 180 / 181 / 189 / 191 / 199
     */
    private static final Pattern CHINA_TELECOM_PATTERN = Pattern.compile("^((1[3,5]3|17[3,7]|18[0-1,9]|19[1,9])\\d{8})|(1349\\d{7})|((174(00|01|02|03|04|05))\\d{6})$");
    /**
     * 北京船舶通信导航有限公司（海事卫星通信）174-9
     */
    private static final Pattern MARITIME_COMMUNICATIONS_PATTERN = Pattern.compile("^(1749)\\d{7}$");
    /**
     * 工业和信息化部应急通信保障中心（应急通信）174-06~12
     */
    private static final Pattern EMERGENCY_COMMUNICATION_PATTERN = Pattern.compile("^(174(06|07|08|09|10|11|12))\\d{6}$");

    /**
     * 获取手机运营商
     *
     * @param phone 手机号
     * @return 当手机号不是手机号或无法匹配运营商时返回null，否则返回对应运营商名称
     */
    @Nullable
    public static String getPhoneOperator(String phone) {
        if (isPhone(phone)) {
            if (CHINA_MOBILE_PATTERN.matcher(phone).matches()) {
                return "中国移动";
            } else if (CHINA_UNICOM_PATTERN.matcher(phone).matches()) {
                return "中国联通";
            } else if (CHINA_TELECOM_PATTERN.matcher(phone).matches()) {
                return "中国电信";
            } else if (MARITIME_COMMUNICATIONS_PATTERN.matcher(phone).matches()) {
                return "北京船舶通信导航有限公司（海事卫星通信）";
            } else if (EMERGENCY_COMMUNICATION_PATTERN.matcher(phone).matches()) {
                return "工业和信息化部应急通信保障中心（应急通信）";
            }
        }
        return null;
    }
}
