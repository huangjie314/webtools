package com.jobcn.controller;

/**
 * Created by winson on 11/02/2017.
 */

import com.jobcn.service.SvnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class PublicController {

    @Autowired
    private SvnService svnService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("")
    public String index() {
        return "redirect:/query";

    }

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping("login")
    public String login() {
        return "public/login";

    }

    /**
     * 执行登录
     *
     * @param model
     * @return
     */
    @RequestMapping("doLogin")
    public String doLogin(Model model, HttpServletResponse response, HttpSession session, String username, String password) {
        boolean pass = false;
        //验证是否登录通过
        pass = svnService.login(username, password);

        if (pass) {
            session.setAttribute("username", username);
            session.setAttribute("password", password);
            return "redirect:/query";
        } else {
            model.addAttribute("tip", "账号或密码错误");
            return "redirect:/login";
        }
    }

    /**
     * 退出系统
     *
     * @param session Session
     * @return
     */
    @RequestMapping(value = "/doLogout")
    public String doLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/query";
    }

    /**
     * 查询
     *
     * @param num  查询前多少条
     * @param path 项目路径(只需要端口以后的路径)
     * @return
     */
    @RequestMapping("query")
    public String query(HttpSession session, Model model,
                        @RequestParam(value = "num", required = false) Integer num,
                        @RequestParam(value = "path", required = false) String path) {
        String username = session.getAttribute("username").toString();
        String password = session.getAttribute("password").toString();
        Map<String, Object> map = svnService.query(num, path, username, password);
        if (map.get("list") == null) {
            session.invalidate();
            return "redirect:/query";
        }
        model.addAllAttributes(map);
        model.addAttribute("path", path);
        return "public/queryReslut";
    }

}
