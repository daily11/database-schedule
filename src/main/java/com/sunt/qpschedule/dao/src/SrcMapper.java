package com.sunt.qpschedule.dao.src;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Chen Yixing
 * @date 2020/11/17 16:09
 */
@Repository
public interface SrcMapper {
    /**
     * 查询表数据
     *
     * @author Chen Yixing
     * @date 2020-11-18 17:23:43
     * @param tbName    表名
     * @param startTime 开始时间
     * @param endTime   结束时间
     **/
    List<Map<String, Object>> listTables(
            @Param("tbName") String tbName,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );
}
