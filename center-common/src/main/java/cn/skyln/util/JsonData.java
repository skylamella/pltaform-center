package cn.skyln.util;

import cn.skyln.enums.BizCodeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

/**
 * @Author: lamella
 * @Date: 2022/11/26/20:05
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonData {
    /**
     * 状态码 0 表示成功
     */

    private Integer code;
    /**
     * 数据
     */
    @JsonIgnore
    private Object data;
    /**
     * 描述
     */
    @JsonIgnore
    private String msg;

    /**
     * 获取远程调用数据
     * 注意事项：
     * 支持多单词下划线专驼峰（序列化和反序列化）
     *
     * @param typeReference
     * @param <T>
     * @return T
     */
    public <T> T getData(TypeReference<T> typeReference) {
        return JSON.parseObject(JSON.toJSONString(data), typeReference);
    }

    /**
     * 成功，不传入数据
     *
     * @return JsonData
     */
    public static JsonData buildSuccess() {
        return JsonData.buildJsonData(0, null, null);
    }

    /**
     * 成功，传入数据
     *
     * @param data 数据
     * @return JsonData
     */
    public static JsonData buildSuccess(Object data) {
        return JsonData.buildJsonData(0, data, null);
    }

    /**
     * 自定义状态码和错误信息
     *
     * @param code 状态码
     * @param msg  信息
     * @return JsonData
     */
    public static JsonData buildJsonData(int code, Object data, String msg) {
        return new JsonData(code, data, msg);
    }

    /**
     * 传入枚举，返回信息
     *
     * @param codeEnum BizCodeEnum
     * @return JsonData
     */
    public static JsonData buildResult(BizCodeEnum codeEnum) {
        return JsonData.buildJsonData(codeEnum.getCode(), null, codeEnum.getMessage());
    }

    /**
     * 传入枚举，返回信息
     *
     * @param codeEnum BizCodeEnum
     * @param data     数据
     * @return JsonData
     */
    public static JsonData buildResult(BizCodeEnum codeEnum, Object data) {
        return JsonData.buildJsonData(codeEnum.getCode(), data, codeEnum.getMessage());
    }
}
