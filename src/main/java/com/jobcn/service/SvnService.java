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
     * 查询
     * @param num 查询前多少条
     * @param Path  项目路径(只需要端口以后的路径)
     * @param author 查询的作者
     * @return
     */
    Map<String,Object> query(Integer num, String Path, String author,String password);
}
