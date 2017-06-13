package com.jobcn.controller;

import com.jobcn.Entity.SvnPath;
import com.jobcn.Entity.SvnUser;
import com.jobcn.repository.SvnPathRepository;
import com.jobcn.repository.SvnUserRepository;
import com.jobcn.service.SvnPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by winson on 17-6-13.
 */
@Controller
public class AdminController {
    @Autowired
    private SvnPathService svnPathService;

    @Autowired
    private SvnPathRepository svnPathRepository;

    @Autowired
    private SvnUserRepository svnUserRepository;

    @RequestMapping("setting")
    public String setting(HttpSession session, Model model){
        SvnUser svnUser = (SvnUser) session.getAttribute("svnUser");
        if ("Admin".equals(svnUser.getRole())){
            List pathList = svnPathRepository.findAll();
            List userList = svnUserRepository.findAll();
            model.addAttribute("paths",pathList);
            model.addAttribute("users",userList);
            return "admin/setting";
        }else {
            return "redirect:/query";
        }
    }

    /**
     * 设置用户权限
     * @param session
     * @param model
     * @param id
     * @param name
     * @param role
     * @return
     */
    @RequestMapping("setUser")
    public String setUser(HttpSession session, Model model,Integer id,String name,String role){
        SvnUser svnUser = (SvnUser) session.getAttribute("svnUser");
        if (!"Admin".equals(svnUser.getRole())){
            return "redirect:/query";
        }
        SvnUser s = new SvnUser();
        s.setId(id);
        s.setName(name);
        s.setRole(role);
        svnUserRepository.save(s);
        return "redirect:/setting";
    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping("delUser")
    public String delUser(HttpSession session, Model model,Integer id){
        SvnUser svnUser = (SvnUser) session.getAttribute("svnUser");
        if (!"Admin".equals(svnUser.getRole())){
            return "redirect:/query";
        }
        svnUserRepository.delete(id);
        return "redirect:/setting";
    }



    /**
     * 设置路径
     * @param session
     * @param model
     * @param id
     * @param path
     * @param replace
     * @return
     */
    @RequestMapping("setPath")
    public String setPath(HttpSession session, Model model,Integer id,String path,String replace){
        SvnUser svnUser = (SvnUser) session.getAttribute("svnUser");
        if (!"Admin".equals(svnUser.getRole())){
            return "redirect:/query";
        }
        SvnPath s = new SvnPath();
        s.setId(id);
        s.setOriginPath(path);
        s.setNewPath(replace);
        svnPathRepository.save(s);
        return "redirect:/setting";
    }

    /**
     * 添加路径
     * @param session
     * @param model
     * @param path
     * @param replace
     * @return
     */
    @RequestMapping("addPath")
    public String addPath(HttpSession session, Model model,String path,String replace){
        SvnUser svnUser = (SvnUser) session.getAttribute("svnUser");
        if (!"Admin".equals(svnUser.getRole())){
            return "redirect:/query";
        }
        SvnPath s = new SvnPath();
        s.setOriginPath(path);
        s.setNewPath(replace);
        SvnPath svnPath = svnPathRepository.save(s);
        if (svnPath!=null)
            svnPathService.reload();
        return "redirect:/setting";
    }


    /**
     * 删除路径
     * @param id
     * @return
     */
    @RequestMapping("delPath")
    public String delPath(HttpSession session, Model model,Integer id){
        SvnUser svnUser = (SvnUser) session.getAttribute("svnUser");
        if (!"Admin".equals(svnUser.getRole())){
            return "redirect:/query";
        }
        svnPathRepository.delete(id);
        return "redirect:/setting";
    }
}
