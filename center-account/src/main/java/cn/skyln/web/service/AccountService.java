package cn.skyln.web.service;

import cn.skyln.util.JsonData;
import cn.skyln.web.model.DO.AccountDO;
import cn.skyln.web.model.REQ.UserLoginREQ;
import cn.skyln.web.model.REQ.UserRegisterREQ;
import cn.skyln.web.model.VO.AccountVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author lamella
 * @description AccountService TODO
 * @since 2024-02-02 21:24
 */
public interface AccountService {

    AccountDO getAccountById(String id);

    AccountDO getAccountByMailOrPhoneOrUsername(String phoneOrMailOrUsername);

    JsonData getAccountForLogin(HttpServletRequest httpServletRequest, UserLoginREQ userLoginREQ);

    JsonData userRegister(UserRegisterREQ userRegisterREQ);

    AccountVO findUserDetail();
}
