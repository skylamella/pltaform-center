package cn.skyln.web.controller;

import cn.skyln.component.CosComponent;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.util.JsonData;
import cn.skyln.web.model.REQ.UserLoginREQ;
import cn.skyln.web.model.REQ.UserRegisterREQ;
import cn.skyln.web.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private CosComponent component;


    @Autowired
    private AccountService accountService;

    @PostMapping("avatar/upload")
    public JsonData uploadUserAvatar(@RequestPart("file") MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String folder = "user/avatar/" + dtf.format(now);
        String uploadUserAvatar = component.uploadFileResult(folder, file, "uploadUserAvatar");
        if (StringUtils.isNotBlank(uploadUserAvatar)) {
            return JsonData.buildResult(BizCodeEnum.OPERATE_SUCCESS, uploadUserAvatar);
        } else {
            return JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
        }
    }

    @PostMapping("login")
    public JsonData login(HttpServletRequest request, @RequestBody UserLoginREQ userLoginREQ) {
        return accountService.getAccountForLogin(request, userLoginREQ);
    }

    @PostMapping("register")
    public JsonData register(@RequestBody UserRegisterREQ userRegisterREQ) {
        return accountService.userRegister(userRegisterREQ);
    }
}
