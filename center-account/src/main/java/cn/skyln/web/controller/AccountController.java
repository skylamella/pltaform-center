package cn.skyln.web.controller;

import cn.skyln.enums.BizCodeEnum;
import cn.skyln.util.JsonData;
import cn.skyln.web.model.DO.AccountDO;
import cn.skyln.web.model.REQ.AccountREQ;
import cn.skyln.web.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author lamella
 * @description DemoController TODO
 * @since 2024-02-02 21:21
 */
@RestController
@RequestMapping("/api/v1/account")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("login")
    public JsonData login(@RequestBody AccountREQ accountREQ) {
        if (!accountREQ.check()) {
            return JsonData.buildResult(BizCodeEnum.DATA_NOT_FOUND_ERROR);
        }
        AccountDO accountDO = accountService.getAccountForLogin(accountREQ.getUsername(), accountREQ.getPassword());
        if (Objects.isNull(accountDO)) {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }
        return JsonData.buildSuccess(accountDO);
    }

    @PostMapping("register")
    public JsonData register(@RequestBody AccountREQ accountREQ) {
        if (!accountREQ.check()) {
            return JsonData.buildResult(BizCodeEnum.DATA_NOT_FOUND_ERROR);
        }
        AccountDO accountDO = accountService.userRegister(accountREQ);
        if (Objects.isNull(accountDO)) {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }
        return JsonData.buildSuccess(accountDO);
    }
}
