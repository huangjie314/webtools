package com.jobcn.controller;

/**
 * Created by winson on 11/02/2017.
 */

import com.jobcn.config.SvnProperties;
import com.jobcn.service.SvnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublicController {

    @Autowired
    private SvnService svnService;

    @Autowired
    private SvnProperties svnProperties;
    /**
     * 首页
     * @param model
     * @return
     */
    @RequestMapping("")
    public String index(Model model) {
        return "/public/index";

    }

    /**
     * 查询
     * @param num 查询前多少条
     * @param path  项目路径(只需要端口以后的路径)
     * @param author 查询的作者
     * @return
     */
    @RequestMapping("query")
    public String query(Model model,
                        @RequestParam(value = "num", required = false) Integer num,
                        @RequestParam(value = "path", required = false) String path,
                        @RequestParam(value = "author", required = false) String author) {
        model.addAllAttributes(svnService.query(num,path,author));
        model.addAttribute("path",path);
        return "/public/queryReslut";

    }

}
