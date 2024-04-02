package cn.skyln.web.service;

import cn.skyln.web.model.DO.AccountDO;
import cn.skyln.web.model.REQ.AccountREQ;
import cn.skyln.web.model.VO.AccountVO;

/**
 * @author lamella
 * @description AccountService TODO
 * @since 2024-02-02 21:24
 */
public interface AccountService {

    AccountDO getAccountById(String id);
    AccountDO getAccountByMailOrPhoneOrUsername(String phoneOrMailOrUsername);

    AccountDO getAccountForLogin(String loginInfo, String pwd);

    AccountDO userRegister(AccountREQ accountREQ);

    AccountVO findUserDetail();
}
