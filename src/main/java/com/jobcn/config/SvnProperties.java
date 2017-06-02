package com.jobcn.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by winson on 2016/12/5.
 */
@Component
@ConfigurationProperties(prefix = "svn")
public class SvnProperties {
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
