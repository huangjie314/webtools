package com.jobcn.service;

import java.util.Map;

/**
 * Created by winson on 17-5-26.
 */
public interface SvnService {
    /**
     * ��ѯ
     * @param num ��ѯǰ������
     * @param Path  ��Ŀ·��(ֻ��Ҫ�˿��Ժ��·��)
     * @param author ��ѯ������
     * @return
     */
    Map<String,Object> query(Integer num, String Path, String author);
}
