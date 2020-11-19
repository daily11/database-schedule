package com.sunt.qpschedule.service.impl;

import com.sunt.qpschedule.dao.dest.DestMapper;
import com.sunt.qpschedule.dao.src.SrcMapper;
import com.sunt.qpschedule.service.IMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Yixing
 * @date 2020/11/17 16:11
 */
@Service
public class MapperServiceImpl implements IMapperService {
    private static final String TAG = "UserInfoServiceImpl";
    private static Logger logger = LoggerFactory.getLogger(TAG);

    private SrcMapper srcUserInfoMapper;
    private DestMapper destUserInfoMapper;

    @Autowired
    public MapperServiceImpl(
            SrcMapper srcUserInfoMapper,
            DestMapper destUserInfoMapper
    ) {
        this.srcUserInfoMapper = srcUserInfoMapper;
        this.destUserInfoMapper = destUserInfoMapper;
    }

    @Override
    public List<Map<String, Object>> listSrcTables(String tbName, List<String> date) {
        return srcUserInfoMapper.listTables(tbName, date.get(0), date.get(1));
    }

    @Override
    public List<Map<String, Object>> listDestTables(String tbName, List<String> date) {
        return destUserInfoMapper.listTables(tbName, date.get(0), date.get(1));
    }

    @Override
    public void insertDestTables(String tbName, List<Map<String, Object>> srcTbList) {
        if (StringUtils.isEmpty(tbName) || srcTbList == null || srcTbList.size() == 0) {
            return;
        }

        for (Map<String, Object> paramMap : srcTbList) {
            List<String> paramKey = new ArrayList<>();
            List<Object> paramValue = new ArrayList<>();

            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                paramKey.add(entry.getKey());
                paramValue.add(entry.getValue());
            }

            // 组装动态SQL语句
            StringBuilder sql = new StringBuilder();
            sql.append("insert into ");
            sql.append(tbName);
            sql.append(" (");
            paramKey.forEach(arg -> sql.append(arg).append(","));
            sql.replace(sql.length() - 1, sql.length(), "");
            sql.append(") values (");
            paramValue.forEach(arg -> sql.append("'").append(arg == null ? 0 : arg).append("'").append(","));
            sql.replace(sql.length() - 1, sql.length(), "");
            sql.append(")");

//            logger.info("拼接的单条sql语句---> " + sql);
            try {
                destUserInfoMapper.insert(sql.toString());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }
}
