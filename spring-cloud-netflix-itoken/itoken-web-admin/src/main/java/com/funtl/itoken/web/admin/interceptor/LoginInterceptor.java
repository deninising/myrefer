package com.funtl.itoken.web.admin.interceptor;

import com.funtl.itoken.common.utils.CookieUtils;
import com.funtl.itoken.common.web.constants.WebConstants;
import com.funtl.itoken.common.web.interceptor.BaseInterceptorMethods;
import com.funtl.itoken.web.admin.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    //需要redis服务做支撑，在校验token时，和创建局部会话时需要从缓存中拿去数据（token令牌是否还可用，user是否已经退出登录）
    @Autowired
    private RedisService redisService;

    //判断是否存在token，若存在则放行进行下一步判断，若没有则直接打回到（带着url）sso登录界面
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return BaseInterceptorMethods.preHandleForLogin(request, response, handler, "http://localhost:8601/" + request.getServletPath());
    }

    //存在token进一步判断登录状态
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        //二次确认token的存在，以防在临界时间点token过期
        String token = CookieUtils.getCookieValue(request, WebConstants.SESSION_TOKEN);
        if (StringUtils.isNotBlank(token)) {
            //校验token的有效性
            String loginCode = redisService.get(token);
            if (StringUtils.isNotBlank(loginCode)) {
                //判断是否首次登录到该子系统，从而确定放行前是否需要创建局部会话
                BaseInterceptorMethods.postHandleForLogin(request, response, handler, modelAndView, redisService.get(loginCode), "http://localhost:8601/" + request.getServletPath());
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
