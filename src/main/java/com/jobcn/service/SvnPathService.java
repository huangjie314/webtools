package com.jobcn.service;

import java.util.Map;

/**
 * Created by winson on 17-5-26.
 */
public interface SvnPathService {
    /**
     * 获取替换的项目路径
     * @return
     */
    Map<String, Object> getPathSetting();

    void reload();
}
