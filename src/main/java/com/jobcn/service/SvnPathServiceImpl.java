package com.jobcn.service;

import com.jobcn.Entity.SvnPath;
import com.jobcn.repository.SvnPathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by winson on 17-6-13.
 */
@Service("svnPathService")
public class SvnPathServiceImpl implements SvnPathService{

    @Autowired
    private SvnPathRepository svnPathRepository;

    static Map<String, Object> pathMap = null;

    @Override
    public Map<String, Object> getPathSetting(){
        if (pathMap==null)
            pathMap=setPathMapFromCache();
        return pathMap;
    }

    private Map<String, Object> setPathMapFromCache(){
        List<SvnPath> list = svnPathRepository.findAll();
        Map map = new HashMap();
        for (SvnPath svnPath:list) {
            String replace =svnPath.getReplace();
            String[] replaceArr =replace.split(";");
            List replaceList = Arrays.asList(replaceArr);
            map.put(svnPath.getPath(),replaceList);
        }
        return map;
    }

    @Override
    public void reload(){
        pathMap=setPathMapFromCache();
    }
}
