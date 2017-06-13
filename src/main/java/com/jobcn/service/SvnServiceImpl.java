package com.jobcn.service;

import com.alibaba.fastjson.JSON;
import com.jobcn.Entity.SvnUser;
import com.jobcn.WebApplication;
import com.jobcn.config.SvnProperties;
import com.jobcn.repository.SvnUserRepository;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by winson on 17-5-26.
 */
@Service("svnService")
public class SvnServiceImpl implements SvnService {
   // private final Logger logger = LoggerFactory.getLogger(SvnServiceImpl.class);

    @Autowired
    private SvnProperties svnProperties;

    @Autowired
    private SvnPathService svnPathService;
    @Autowired
    private SvnUserRepository svnUserRepository;

    static private Map<String, Object> authors = null;
    @Override
    public boolean login(String author, String password) {
        String cmd = "svn log svn://" + svnProperties.getUrl() + " -l 1 -v --xml";
        cmd = cmd + " --username " + author + " --password " + password;
        String xml = exeCommond(cmd);
        //<?xml version="1.0" encoding="UTF-8"?><log>  账号密码不正确返回的格式
        if (xml != null && !"".equals(xml) && !"<?xml version=\"1.0\" encoding=\"UTF-8\"?><log>".equals(xml)) {
            return true;
        }
        return false;
    }

    @Override
    public SvnUser checkUser(Integer id) {
        SvnUser svnUser = svnUserRepository.findOne(id);
        if (svnUser == null) {
            svnUser =new SvnUser();
            svnUser.setId(id);
            svnUser.setRole("Normal");
            svnUser = svnUserRepository.save(svnUser);
        }
        return svnUser;
    }

    @Override
    public Map<String, Object> query(Integer num, String path, String username, String password, String start, String end, String author) {
        Map<String, Object> map = new HashMap<String, Object>();
        //svn log svn://192.168.61.155:11001 -l 100 -v --xml --username JCNEP7340 --password vincent -r{2016-06-27T12:00:00}:{2016-06-28T13:00:00}
        StringBuilder cmd = new StringBuilder();
        cmd.append("svn log svn://" + svnProperties.getUrl() + "/");
        //文件路径
        if (path != null && !"".equals(path)) {
            path = path.replaceFirst(",", "/");
            cmd.append(path + "/");
        }
        //数量
        if (num == null || num == 0) {
            cmd.append(" -l 300 -v --xml ");
        } else {
            cmd.append(" -l " + num + " -v --xml ");
        }
        cmd.append(" --username " + username + " --password " + password);

        if (author != null && !"".equals(author)) {
            if (!"all".equals(author)) {
                if (!author.startsWith("JCNEP"))
                    author = "JCNEP" + author;
                cmd.append(" --search " + author);
            }
        } else {
            if (username != null && !"".equals(username)) {
                if (!username.startsWith("JCNEP"))
                    username = "JCNEP" + username;
                cmd.append(" --search " + username);
            }
        }
        //日期
        if (start != null && end != null && !start.isEmpty() && !end.isEmpty()) {
            cmd.append(" -r{" + end + "T23:59:59}:{" + start + "T00:00:00} ");
        }
        //logger.info("cmd======>"+cmd.toString());
        String xml = exeCommond(cmd.toString());
        //logger.info("xml======>"+xml);
        //<?xml version="1.0" encoding="UTF-8"?><log>  账号密码不正确返回的格式
        if ("<?xml version=\"1.0\" encoding=\"UTF-8\"?><log>".equals(xml)) {
            return map;
        }
        List<Map<String, Object>> list = null;

        try {
            list = xml2list(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (list != null) {
            map.put("list", list);
            List<String> authors = new ArrayList();
            for (Map<String, Object> row : list) {
                if (!authors.contains(row.get("author").toString())) {
                    authors.add(row.get("author").toString());
                }
            }
            Collections.sort(authors);
            map.put("authors", authors);
            if (list.size() > 0) {
                map.put("end", list.get(0).get("date").toString().substring(0, 10));
                map.put("start", list.get(list.size() - 1).get("date").toString().substring(0, 10));
            }
            {

            }
        }
        return map;
    }

    /**
     * 执行命令行,返回文本
     *
     * @param cmd
     * @return
     */
    public String exeCommond(String cmd) {
        String[] cmds = new String[0];
        try {
            cmds = new String[]{"/bin/sh", "-c", new String(cmd.getBytes(), "UTF8")};
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Process ps = null;
        try {
            ps = Runtime.getRuntime().exec(cmds);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(ps.getInputStream(), "UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = sb.toString();
        return result;
    }


    /**
     * 将svn返回的xml文本转成map
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public List<Map<String, Object>> xml2list(String xml) throws DocumentException {
        if (authors == null) {
            authors = getAuthor();
        }
        List<String> resultList = new ArrayList();
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        List<Element> nodes = root.elements("logentry");
        List<Map<String, Object>> result = new ArrayList();
        for (Element node : nodes) {
            Map<String, Object> nodeMap = new HashMap();
            String author = node.element("author").getText();
            String date = node.element("date").getText();
            String msg = node.element("msg").getText();
            //处理msg
            if (!msg.startsWith("--")) {
                if (msg.startsWith("-")) {
                    msg = "-" + msg;
                } else {
                    msg = "--" + msg;
                }
            }
            if (authors.containsKey(author)) {
                if (!msg.trim().endsWith(authors.get(author).toString())) {
                    msg = msg + " " + authors.get(author).toString();
                }
            }
            List<Element> paths = node.element("paths").elements("path");
            List<String> pathListAfter = new ArrayList();
            List<String> pathListBefore = new ArrayList();
            for (Element pathElement : paths) {
                String path = pathElement.getText();
                pathListBefore.add(path);
                List<String> pathList = replace(path);
                for (String path1 : pathList) {
                    pathListAfter.add(path1);
                }
            }
            nodeMap.put("author", author);
            nodeMap.put("date", formatDateTime(date));
            nodeMap.put("msg", msg);
            nodeMap.put("paths_before", pathListBefore);
            nodeMap.put("paths_after", pathListAfter);
            result.add(nodeMap);
        }

        return result;
    }

    private String formatDateTime(String date) {
        Date dateTime = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            dateTime = sdf1.parse(date.substring(0, date.indexOf(".")).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(dateTime);
        ca.add(Calendar.HOUR_OF_DAY, 8);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf2.format(ca.getTime());
    }

    private List<String> replace(String path) {
        Map<String, Object> setting  = svnPathService.getPathSetting();
        List<String> resultList = new ArrayList();
        for (Map.Entry<String, Object> entry : setting.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                Object value = entry.getValue();
                if (value instanceof List) {
                    List<String> valueList = (List<String>) value;
                    for (String value1 : valueList) {
                        String newPath = path.replaceFirst(entry.getKey(), value1) + " (通用类)";
                        newPath = newPath + "\n";
                        resultList.add(newPath);
                    }
                } else {
                    String newPath = path.replaceFirst(entry.getKey(), entry.getValue().toString());
                    if (newPath.endsWith("properties"))
                        newPath = newPath + "(配置文件)";
                    newPath = newPath + "\n";
                    resultList.add(newPath);
                }
                return resultList;
            }
        }
        resultList.add(path + " (注:转换失败)\n");
        return resultList;
    }

    /**
     * 获取配置文件中的正则匹配
     *
     * @return
     */
    private Map<String, Object> getSetting() {
        InputStream inputStream = WebApplication.class.getClassLoader().getResourceAsStream("setting.json");
        String value = null;
        try {
            value = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map settingMap = JSON.parseObject(value, Map.class);
        return settingMap;
    }

    /**
     * 获取配置文件中的作者
     *
     * @return
     */
    private Map<String, Object> getAuthor() {
        Map authorMap =new HashMap();
        List<SvnUser> list = svnUserRepository.findAll();
        for (SvnUser svnUser:list) {
            authorMap.put(svnUser.getId()<1000?"JCNEP0"+svnUser.getId():"JCNEP"+svnUser.getId(),svnUser.getName());
        }
        return authorMap;
    }
}
