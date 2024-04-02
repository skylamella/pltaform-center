package cn.skyln.exception;

import cn.skyln.enums.BizCodeEnum;
import cn.skyln.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: lamella
 * @Date: 2022/08/31/22:15
 * @Description:
 */
@ControllerAdvice
@Slf4j
public class BizExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData handler(Exception e) {
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            log.error("【业务异常】 {}", e);
            return JsonData.buildJsonData(bizException.getCode(), null, bizException.getMsg());
        } else {
            log.error("【系统异常】 {}", e);
            return JsonData.buildResult(BizCodeEnum.SYSTEM_ERROR);
        }
    }

}
