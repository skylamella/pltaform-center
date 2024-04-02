package cn.skyln.web.service.impl;

import cn.skyln.interceptor.LoginInterceptor;
import cn.skyln.model.LoginUser;
import cn.skyln.web.dao.mapper.AccountMapper;
import cn.skyln.web.dao.repo.AccountRepo;
import cn.skyln.web.model.DO.AccountDO;
import cn.skyln.web.model.REQ.AccountREQ;
import cn.skyln.web.model.VO.AccountVO;
import cn.skyln.web.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static cn.skyln.util.SecurityUtil.MD5WithSecret;

/**
 * @author lamella
 * @description AccountServiceImpl TODO
 * @since 2024-02-02 21:25
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public AccountDO getAccountById(String id) {
        return accountRepo.getAccountById(id);
    }

    @Override
    public AccountDO getAccountByMailOrPhoneOrUsername(String phoneOrMailOrUsername) {
        return accountRepo.getAccountByMailOrPhoneOrUsername(phoneOrMailOrUsername, phoneOrMailOrUsername, phoneOrMailOrUsername);
    }

    @Override
    public AccountDO getAccountForLogin(String loginInfo, String pwd) {
        AccountDO accountDO = accountRepo.getAccountByMailOrPhoneOrUsername(loginInfo, loginInfo, loginInfo);
        if (Objects.nonNull(accountDO)) {
            String password = MD5WithSecret(accountDO.getPwd(), accountDO.getSecret());
            if (StringUtils.equals(pwd, password)) {
                return accountDO;
            }
        }
        return null;
    }

    @Override
    public AccountDO userRegister(AccountREQ accountREQ) {
        String loginInfo = accountREQ.getUsername();
        AccountDO accountDO = accountRepo.getAccountByMailOrPhoneOrUsername(loginInfo, loginInfo, loginInfo);
        if (Objects.isNull(accountDO)) {

        }
        return null;
    }

    @Override
    public AccountVO findUserDetail() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        return null;
    }
}
