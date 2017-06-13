package com.jobcn.controller;

/**
 * Created by winson on 11/02/2017.
 */

import com.jobcn.Entity.SvnUser;
import com.jobcn.repository.SvnUserRepository;
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

    @Autowired
    private SvnUserRepository svnUserRepository;

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
    public String login(Model model, @RequestParam(value = "tip", required = false) String tip) {
        if (tip != null)
            model.addAttribute("tip", tip);
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
            //判断是否有对于的权限记录没有则取,没有则添加
            Integer id = Integer.parseInt(username.substring(5, username.length()));
            SvnUser svnUser = svnService.checkUser(id);
            session.setAttribute("username", username);
            session.setAttribute("password", password);
            session.setAttribute("svnUser", svnUser);
            return "redirect:/query";
        } else {
            model.addAttribute("tip", "账号或密码错误!");
            return "public/login";
        }
    }

    /**
     * 设置姓名
     *
     * @return
     */
    @RequestMapping("setName")
    public String setName(HttpSession session, Model model,  String name) {
        SvnUser svnUser = (SvnUser) session.getAttribute("svnUser");
        svnUser.setName(name);
        svnUser = svnUserRepository.save(svnUser);
        if (svnUser!=null&&svnUser.getName()!=null){
            session.setAttribute("svnUser",svnUser);
        }
        return "redirect:/query";
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
                        @RequestParam(value = "path", required = false) String path,
                        @RequestParam(value = "start", required = false) String start,
                        @RequestParam(value = "end", required = false) String end,
                        @RequestParam(value = "author", required = false) String author) {

        String username = session.getAttribute("username").toString();
        String password = session.getAttribute("password").toString();
        SvnUser svnUser = (SvnUser) session.getAttribute("svnUser");


        switch (svnUser.getRole()) {
            case "Normal"://①如果是Normal用户,不支持其他用户的搜索
                author = null;
                break;
            case "Test"://②如果是Test用户,且搜索author为空或自己时,默认搜索全部
                if (author == null || "".equals(author) || username.equals(author)) {
                    author = "all";
                    num=100;
                }
                break;
            default:
                break;
        }
        Map<String, Object> map = svnService.query(num, path, username, password, start, end, author);
        if (map.get("list") == null) {
            //session.invalidate();
            return "redirect:/query";
        }
        model.addAllAttributes(map);
        model.addAttribute("path", path);
        model.addAttribute("author", author);
        return "public/queryReslut";
    }

}
