package cn.skyln.interceptor;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.model.LoginUser;
import cn.skyln.util.CheckUtil;
import cn.skyln.util.CommonUtils;
import cn.skyln.util.JsonData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

/**
 * @Author: lamella
 * @Date: 2022/09/04/20:36
 * @Description:
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    public static ThreadLocal<LoginUser> threadLocal = new InheritableThreadLocal<>();

    /**
     * 24小时有效
     */
    private static final long CODE_EXPIRED = 24;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 读取token
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }

        // 判定token是否为空
        if (StringUtils.isEmpty(token)) {
            CommonUtils.renderJson(response, JsonData.buildResult(BizCodeEnum.ACCOUNT_UNLOGIN_ERROR));
            return false;
        }

        Object loginId = StpUtil.getLoginIdByToken(token);

        if (Objects.isNull(loginId)) {
            CommonUtils.renderJson(response, JsonData.buildResult(BizCodeEnum.ACCOUNT_UNLOGIN_ERROR));
            return false;
        }

        SaSession saSession = StpUtil.getSessionByLoginId(loginId);
        if (saSession.getInt("safe_mode") == 1 && !checkIp(saSession, request)) {
            CommonUtils.renderJson(response, JsonData.buildResult(BizCodeEnum.ACCOUNT_SAFE_MODE_RELOGIN));
            return false;
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setHeadImg(saSession.getString("head_img"));
        loginUser.setMail(saSession.getString("mail"));
        loginUser.setName(saSession.getString("name"));
        loginUser.setId(String.valueOf(loginId));
        threadLocal.set(loginUser);
        return true;
    }


    /**
     * 安全模式IP校验
     *
     * @param saSession SaSession
     * @param request   HttpServletRequest
     * @return 未开启安全模式返回true，否则校验token中的IP是否与当前登录IP相同
     */
    private boolean checkIp(SaSession saSession, HttpServletRequest request) {
        if (CheckUtil.checkIp(saSession.getString("ip"))) {
            return StringUtils.equals(CommonUtils.getIpAddr(request), saSession.getString("ip"));
        }
        return true;
    }
}
