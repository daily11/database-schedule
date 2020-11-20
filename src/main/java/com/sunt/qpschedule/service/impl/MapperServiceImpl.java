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
        try {
            if (date == null) {
                return srcUserInfoMapper.listTables(tbName, null, null);
            } else {
                try {
                    return srcUserInfoMapper.listTables(tbName, date.get(0), date.get(1));
                } catch (Exception e) {
                    // 有可能表是没有gmt_create字段的，因此会报错，报错的时候，我们制定这个字段起始/截止为null，重新查询！
                    return srcUserInfoMapper.listTables(tbName, null, null);
                }
            }
        } catch (Exception e) {
            logger.error(tbName + " 不存在！");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> listDestTables(String tbName, List<String> date) {
        try {
            if (date == null) {
                return destUserInfoMapper.listTables(tbName, null, null);
            } else {
                try {
                    return destUserInfoMapper.listTables(tbName, date.get(0), date.get(1));
                } catch (Exception e) {
                    // 有可能表是没有gmt_create字段的，因此会报错，报错的时候，我们制定这个字段起始/截止为null，重新查询！
                    return destUserInfoMapper.listTables(tbName, null, null);
                }
            }
        } catch (Exception e) {
            logger.error(tbName + "不存在！");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void insertDestTables(String tbName, List<Map<String, Object>> srcTbList) {
        if (StringUtils.isEmpty(tbName) || srcTbList == null || srcTbList.size() == 0) {
            return;
        }

        for (Map<String, Object> paramMap : srcTbList) {
            StringBuilder sql1 = new StringBuilder();
            StringBuilder sql2 = new StringBuilder();

            sql1.append("insert into ");
            sql1.append(tbName);
            sql1.append(" (");

            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if (entry.getValue() != null) {
                    sql1.append(entry.getKey()).append(",");
                    if (entry.getValue() instanceof Boolean) {
                        sql2.append("'").append(entry.getValue().equals(true) ? 1 : 0).append("'").append(",");
                    } else {
                        sql2.append("\"").append(entry.getValue()).append("\"").append(",");
                    }
                }
            }
            sql1.replace(sql1.length() - 1, sql1.length(), "");
            sql1.append(") values (");

            sql2.replace(sql2.length() - 1, sql2.length(), "");
            sql2.append(")");

            String sql = sql1.append(sql2).toString();
//            logger.info("拼接的单条sql语句---> " + sql);
            try {
                destUserInfoMapper.insert(sql);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }
}
