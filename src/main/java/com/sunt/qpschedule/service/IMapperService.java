package com.sunt.qpschedule.service;

import java.util.List;
import java.util.Map;

/**
 * @author Chen Yixing
 * @date 2020/11/17 16:11
 */
public interface IMapperService {
    /**
     * 查询源库表数据
     *
     * @author Chen Yixing
     * @date 2020-11-18 17:22:06
     * @param tbName    表名
     * @param date      时间范围
     **/
    List<Map<String, Object>> listSrcTables(String tbName, List<String> date);

    /**
     * 查询目标库表数据
     *
     * @author Chen Yixing
     * @date 2020-11-18 17:22:06
     * @param tbName    表名
     * @param date      时间范围
     **/
    List<Map<String, Object>> listDestTables(String tbName, List<String> date);

    /**
     * 插入目标库数据
     *
     * @author Chen Yixing
     * @date 2020-11-18 17:23:21
     * @param tbName    表名
     * @param srcTbList 源库数据
     **/
    void insertDestTables(String tbName, List<Map<String, Object>> srcTbList);
}
