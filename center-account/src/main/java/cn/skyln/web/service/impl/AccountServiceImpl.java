package cn.skyln.web.service.impl;

import cn.skyln.constant.TimeConstant;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.interceptor.LoginInterceptor;
import cn.skyln.model.LoginUser;
import cn.skyln.util.CommonUtils;
import cn.skyln.util.JsonData;
import cn.skyln.util.RsaUtils;
import cn.skyln.web.dao.mapper.AccountMapper;
import cn.skyln.web.dao.repo.AccountRepo;
import cn.skyln.web.feignClient.CouponFeignService;
import cn.skyln.web.model.DO.AccountDO;
import cn.skyln.web.model.DTO.NewUserCouponDTO;
import cn.skyln.web.model.REQ.UserLoginRequest;
import cn.skyln.web.model.REQ.UserRegisterRequest;
import cn.skyln.web.model.VO.AccountVO;
import cn.skyln.web.service.AccountService;
import cn.skyln.web.service.NotifyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.skyln.util.SecurityUtil.MD5WithSecret;

/**
 * @author lamella
 * @description AccountServiceImpl TODO
 * @since 2024-02-02 21:25
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private NotifyService notifyService;

    @Resource(name = "cacheDbTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CouponFeignService couponFeignService;

    @Override
    public AccountDO getAccountById(String id) {
        return accountRepo.getAccountById(id);
    }

    @Override
    public AccountDO getAccountByMailOrPhoneOrUsername(String phoneOrMailOrUsername) {
        return accountRepo.getAccountByMailOrPhoneOrUsername(phoneOrMailOrUsername, phoneOrMailOrUsername, phoneOrMailOrUsername);
    }

    @Override
    public JsonData getAccountForLogin(HttpServletRequest httpServletRequest, UserLoginRequest userLoginRequest) {
        // 判断传递的邮箱号是否为空
        if (StringUtils.isEmpty(userLoginRequest.getMail())) {
            log.error("[{}] {}",
                    BizCodeEnum.ACCOUNT_NOT_EXIST_ERROR.getCode(),
                    BizCodeEnum.ACCOUNT_NOT_EXIST_ERROR.getMessage());
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_NOT_EXIST_ERROR);
        }
        // 判断传递的密码是否为空
        if (StringUtils.isEmpty(userLoginRequest.getPwd())) {
            log.error("[{}] {}",
                    BizCodeEnum.ACCOUNT_PWD_NOT_EXIST_ERROR.getCode(),
                    BizCodeEnum.ACCOUNT_PWD_NOT_EXIST_ERROR.getMessage());
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_NOT_EXIST_ERROR);
        }
        // 判断账号是否存在
        List<AccountDO> list = accountMapper.selectList(new QueryWrapper<AccountDO>().eq("mail", userLoginRequest.getMail()));
        if (list == null || list.size() <= 0) {
            log.error("[{}] {}",
                    BizCodeEnum.ACCOUNT_LOGIN_ERROR.getCode(),
                    BizCodeEnum.ACCOUNT_LOGIN_ERROR.getMessage());
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_LOGIN_ERROR);
        }
        AccountDO userDO = list.get(0);
        // 判断密码是否正确
        String md5Crypt = Md5Crypt.md5Crypt(userLoginRequest.getPwd().getBytes(), userDO.getSecret());
        if (!StringUtils.equals(userDO.getPwd(), md5Crypt)) {
            log.error("[{}] {}",
                    BizCodeEnum.ACCOUNT_LOGIN_ERROR.getCode(),
                    BizCodeEnum.ACCOUNT_LOGIN_ERROR.getMessage());
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_LOGIN_ERROR);
        }
        // 登录成功，生成JWT token
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(userDO, loginUser);
        try {
            String token = "";
            String refreshToken = "";
            // 1：开启安全模式
            if (userLoginRequest.getSafeMode() == 1) {
                String ipAddr = CommonUtils.getIpAddr(httpServletRequest);
//                token = JWTUtils.generateToken(loginUser, RsaUtils.getPrivateKey(), ipAddr);
//                refreshToken = JWTUtils.generateRefreshToken(loginUser, RsaUtils.getPrivateKey(), ipAddr);
            } else {
//                token = JWTUtils.generateToken(loginUser, RsaUtils.getPrivateKey());
//                refreshToken = JWTUtils.generateRefreshToken(loginUser, RsaUtils.getPrivateKey());
            }
            redisTemplate.opsForValue().set(token, refreshToken, TimeConstant.EXPIRATION_TIME_HOUR, TimeUnit.HOURS);
            log.info("[{}] \"{}\"{}",
                    BizCodeEnum.LOGIN_SUCCESS.getCode(),
                    userLoginRequest.getMail(),
                    BizCodeEnum.LOGIN_SUCCESS.getMessage());
            return JsonData.buildResult(BizCodeEnum.LOGIN_SUCCESS, token);
        } catch (Exception e) {
            log.error("[{}] {}",
                    BizCodeEnum.ACCOUNT_LOGIN_ERROR.getCode(),
                    BizCodeEnum.ACCOUNT_LOGIN_ERROR.getMessage());
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_LOGIN_ERROR);
        }
    }

    @Override
    public JsonData userRegister(UserRegisterRequest userRegisterRequest) {
        // 判断邮箱是否传入
        if (StringUtils.isEmpty(userRegisterRequest.getMail())) {
            log.error("[{}] {}",
                    BizCodeEnum.ACCOUNT_NOT_EXIST_ERROR.getCode(),
                    BizCodeEnum.ACCOUNT_NOT_EXIST_ERROR.getMessage());
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_NOT_EXIST_ERROR);
        }
        // 判断传递的密码是否为空
        if (StringUtils.isEmpty(userRegisterRequest.getPwd()) || StringUtils.isEmpty(userRegisterRequest.getRePwd())) {
            log.error("[{}] {}",
                    BizCodeEnum.ACCOUNT_PWD_NOT_EXIST_ERROR.getCode(),
                    BizCodeEnum.ACCOUNT_PWD_NOT_EXIST_ERROR.getMessage());
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_NOT_EXIST_ERROR);
        }
        // 判断输入的密码和确认密码是否相同
        if (!StringUtils.equals(userRegisterRequest.getPwd(), userRegisterRequest.getRePwd())) {
            log.error("[{}] {}",
                    BizCodeEnum.ACCOUNT_REGISTER_PWD_ERROR.getCode(),
                    BizCodeEnum.ACCOUNT_REGISTER_PWD_ERROR.getMessage());
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REGISTER_PWD_ERROR);
        }
        // 判断验证码是否正确
        if (!notifyService.checkCode("REGISTRATION", userRegisterRequest.getMail(), userRegisterRequest.getCode())) {
            log.error("[{}] {}",
                    BizCodeEnum.CODE_ERROR.getCode(),
                    BizCodeEnum.CODE_ERROR.getMessage());
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }
        AccountDO userDO = new AccountDO();
        BeanUtils.copyProperties(userRegisterRequest, userDO);
        // 密码加盐
        userDO.setSecret("$1$" + CommonUtils.getRandomStr(8));
        // 密码加密
        userDO.setPwd(Md5Crypt.md5Crypt(userRegisterRequest.getPwd().getBytes(), userDO.getSecret()));
        // 邮箱唯一性校验
        if (checkUnique(userDO.getMail())) {
            int insert = accountMapper.insert(userDO);
            if (insert == 1) {
                // TODO 新用户注册福利发放
                userRegisterInitTask(userDO);
                log.info("[{}] \"{}\"{}",
                        BizCodeEnum.ACCOUNT_REGISTER_SUCCESS.getCode(),
                        userRegisterRequest.getMail(),
                        BizCodeEnum.ACCOUNT_REGISTER_SUCCESS.getMessage());
                return JsonData.buildResult(BizCodeEnum.ACCOUNT_REGISTER_SUCCESS);
            } else {
                log.error("[{}] {}",
                        BizCodeEnum.ACCOUNT_REGISTER_ERROR.getCode(),
                        BizCodeEnum.ACCOUNT_REGISTER_ERROR.getMessage());
                return JsonData.buildResult(BizCodeEnum.ACCOUNT_REGISTER_ERROR);
            }
        } else {
            log.error("[{}] {}",
                    BizCodeEnum.ACCOUNT_REPEAT.getCode(),
                    BizCodeEnum.ACCOUNT_REPEAT.getMessage());
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REPEAT);
        }
    }

    @Override
    public AccountVO findUserDetail() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AccountDO accountDO = getAccountById(loginUser.getId());
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(accountDO, accountVO);
        return accountVO;
    }

    private boolean checkUnique(String mail) {
        return accountMapper.selectList(new QueryWrapper<AccountDO>().eq("mail", mail)).isEmpty();
    }

    private void userRegisterInitTask(AccountDO userDO) {
        NewUserCouponDTO newUserCouponDTO = NewUserCouponDTO.builder().userId(userDO.getId()).userName(userDO.getName()).build();
        JsonData jsonData = couponFeignService.intiNewUserCoupon(newUserCouponDTO);
        if (jsonData.getCode() == 0) {
            log.info("[发放新用户注册优惠券成功] 用户：{}，结果：{}", newUserCouponDTO, jsonData);
        } else {
            log.error("[发放新用户注册优惠券失败] 用户：{}，结果：{}", newUserCouponDTO, jsonData);
            // TODO 放入消息队列重新执行
//            throw new RuntimeException(String.format("[发放新用户注册优惠券失败] 用户：%s，结果：%s", newUserCouponRequest, jsonData));
        }
    }
}
