package com.jobcn.service;

import java.util.Map;

/**
 * Created by winson on 17-5-26.
 */
public interface SvnService {
    /**
     * 验证账号密码
     * @param author
     * @param password
     * @return
     */
    boolean login(String author,String password);

    /**
     *
     * @param num 查询前多少条
     * @param Path 项目路径(只需要端口以后的路径)
     * @param username
     * @param password
     * @param start 开始时间
     * @param end 结束时间
     * @param author 需要查询的author,all为查询所有
     * @return
     */
    Map<String,Object> query(Integer num, String Path, String username,String password,String start,String end,String author);
}
