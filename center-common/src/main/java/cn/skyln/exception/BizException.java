package cn.skyln.exception;

import cn.skyln.enums.BizCodeEnum;
import lombok.Data;

/**
 * @Author: lamella
 * @Date: 2022/11/26/20:21
 * @Description:
 */
@Data
public class BizException extends RuntimeException{

    private Integer code;
    private String msg;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public BizException(BizCodeEnum bizCodeEnum) {
        super(bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMessage();
    }

}
