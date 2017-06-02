package com.jobcn.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录认证的拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * Handler执行完成之后调用这个方法
     */
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception exc)
            throws Exception {

    }

    /**
     * Handler执行之后，ModelAndView返回之前调用这个方法
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * Handler执行之前调用这个方法
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        //获取请求的URL
        String url = request.getRequestURI();
        String username =null;
        //不拦截静态资源
        if (url.indexOf("js") >= 0||url.indexOf("css") >= 0||url.indexOf("jpg") >= 0) {
            return true;
        }

        //不拦截登录页面和登录操作
        if (url.indexOf("login") >= 0||url.indexOf("doLogin") >= 0) {
            return true;
        }

        //获取Session
        HttpSession session = request.getSession();
        username = (String) session.getAttribute("username");

        if (username != null) {
            return true;
        }

        //不符合条件的，跳转到登录界面
        request.getRequestDispatcher("/login").forward(request, response);
        return false;
    }

}
