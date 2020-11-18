package com.sunt.qpschedule.config;

import java.util.List;
import java.util.Map;

/**
 * @author Chen Yixing
 * @date 2020/11/17 17:31
 *
 * 存储application.yml配置文件的类
 */
public class ApplicationConfig {
    // 实时表数据
    private Map<String, List<String>> synchronizedReal;

    // 历史表数据
    private List<String> synchronizedLog;

    // 查询日期范围
    private List<String> date;

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public Map<String, List<String>> getSynchronizedReal() {
        return synchronizedReal;
    }

    public void setSynchronizedReal(Map<String, List<String>> synchronizedReal) {
        this.synchronizedReal = synchronizedReal;
    }

    public List<String> getSynchronizedLog() {
        return synchronizedLog;
    }

    public void setSynchronizedLog(List<String> synchronizedLog) {
        this.synchronizedLog = synchronizedLog;
    }
}
