package com.funtl.itoken.service.sso.controller;

import com.funtl.itoken.common.domain.TbSysUser;
import com.funtl.itoken.common.utils.CookieUtils;
import com.funtl.itoken.common.utils.MapperUtils;
import com.funtl.itoken.service.sso.service.LoginService;
import com.funtl.itoken.service.sso.service.consumer.RedisCacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private RedisCacheService redisService;

    @Autowired
    private LoginService loginService;

    /**
     * 跳转登录页
     *
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(
            @RequestParam(required = false) String url,
            HttpServletRequest request, Model model) {
        String token = CookieUtils.getCookieValue(request, "token");

        // token 不为空可能已登录
        if (StringUtils.isNotBlank(token)) {
            //去redis中校验token的有效性
            String loginCode = redisService.get(token);
            if (StringUtils.isNotBlank(loginCode)) {
                //继续去redis中校验通过loginCode能否得到一个对应的用户对象的json数据
                String json = redisService.get(loginCode);
                if (StringUtils.isNotBlank(json)) {
                    try {
                        TbSysUser tbSysUser = MapperUtils.json2pojo(json, TbSysUser.class);
                        // 已登录
                        if (tbSysUser != null) {
                            //如果有明确的指定访问地址，则重定向到该地址
                            if (StringUtils.isNotBlank(url)) {
                                return "redirect:" + url;
                            }
                        }
                        // 如果没有url,将登录信息传到登录页即可
                        model.addAttribute("tbSysUser", tbSysUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //没有token一定没有登录，回到登录页进行登录
        if (StringUtils.isNotBlank(url)) {
            model.addAttribute("url", url);
        }
        return "login";
    }

    /**
     * 登录业务
     *
     * @param loginCode
     * @param password
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(
            @RequestParam(required = true) String loginCode,
            @RequestParam(required = true) String password,
            @RequestParam(required = false) String url,
            HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        TbSysUser tbSysUser = loginService.login(loginCode, password);

        // 登录失败
        if (tbSysUser == null) {
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误，请重新输入");
        }

        // 登录成功
        else {
            String token = UUID.randomUUID().toString();

            // 将 Token 放入缓存,创建全局会话中的token----loginCode(用户名)。
            String result = redisService.put(token, loginCode, 60 * 60 * 24);
            //判断是否成功放入redis中
            if (StringUtils.isNotBlank(result) && "ok".equals(result)) {
                //zaicookie中存入token
                CookieUtils.setCookie(request, response, "token", token, 60 * 60 * 24);
                if (StringUtils.isNotBlank(url)) {
                    //如果有明确的指定访问地址，则重定向到该地址
                    return "redirect:" + url;
                }
            }

            // 熔断处理
            else {
                redirectAttributes.addFlashAttribute("message", "服务器异常，请稍后再试");
            }
        }
        //重定向回login页，此时全局会话已经创建成功
        return "redirect:/login";
    }

    /**
     * 注销
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String url, Model model) {
        try {
            CookieUtils.deleteCookie(request, response, "token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //最好也把redis中的token删除即可
        if (StringUtils.isNotBlank(url)) {
            return "redirect:/login?url=" + url;
        } else {
            return "redirect:/login";
        }
    }
}
