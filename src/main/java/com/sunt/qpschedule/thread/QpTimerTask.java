package com.sunt.qpschedule.thread;

import com.sunt.qpschedule.config.Config;
import com.sunt.qpschedule.config.SuntApplicationContext;
import com.sunt.qpschedule.service.IMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Chen Yixing
 * @date 2020/11/17 15:13
 */
public class QpTimerTask extends TimerTask {
    private static final String TAG = "QpTimerTask";
    private static Logger logger = LoggerFactory.getLogger(TAG);
    // 用于校验当天是否已经执行过，默认没有
    private static volatile boolean QP_EXECUTED_FLAG = false;
    // 每月指定的执行日期
    private static volatile int QP_DAY = 19;

    private Config config;
    private IMapperService iUserInfoService;

    public QpTimerTask() {
        SuntApplicationContext.getInstance().initialize();
        iUserInfoService = SuntApplicationContext.getInstance().getBeanInstance(IMapperService.class);
        config = SuntApplicationContext.getInstance().getBeanInstance(Config.class);
    }

    public void run() {
        // 查询今天日期
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day != QP_DAY) {
            QP_EXECUTED_FLAG = false;
        }

        if (!QP_EXECUTED_FLAG && day == QP_DAY) {
            logger.info("同步任务开始！");

            // 当天不再执行同步任务
            QP_EXECUTED_FLAG = true;

//            logger.info("获取配置文件值案例---> " + config.getApplicationConfig().getSynchronizedReal());

            // 待同步的时间段
            List<String> date = config.getApplicationConfig().getDate();
            if (date == null) {
                // 默认同步前七天的数据
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = new ArrayList<>();
                calendar.set(Calendar.DAY_OF_MONTH, day - 7);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                Date startTime = calendar.getTime();
                date.add(simpleDateFormat.format(startTime));
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                Date endTime = calendar.getTime();
                date.add(simpleDateFormat.format(endTime));
            }

            // 实时表同步
            Map<String, List<String>> synchronizedReal = config.getApplicationConfig().getSynchronizedReal();
            if (synchronizedReal != null) {
                for (Map.Entry<String, List<String>> map : synchronizedReal.entrySet()) {
                    synchronizedReal(map.getKey(), map.getValue(), date);
                }
            }

            // 历史表同步
            List<String> synchronizedLog = config.getApplicationConfig().getSynchronizedLog();
            if (synchronizedLog != null) {
                for (String tbName : synchronizedLog) {
                    synchronizedLog(tbName, date);
                }
            }
        }
    }

    private void synchronizedReal(String tbName, List<String> tbValid,  List<String> date) {
        if (StringUtils.isEmpty(tbName)) {
            return;
        }
        if (tbValid == null || tbValid.size() == 0) {
            synchronizedLog(tbName, date);
            return;
        }

        // 查询源数据
        List<Map<String, Object>> srcTbList = iUserInfoService.listSrcTables(tbName, date);
        List<Map<String, Object>> srcTbListCopy = new ArrayList<>();
        logger.info("源数据---> " + srcTbList);

        // 查询目标数据
        List<Map<String, Object>> destTbList = iUserInfoService.listDestTables(tbName, date);
        logger.info("目标数据---> " + destTbList);

        /**
         *  目标库中待校验属性对应的值列表
         *  存储格式：
         *  {
         *      "name_age" : ["chen_18", "yang_18"],
         *      "name" : ["chen", "yang"]
         *  }
         **/
        Map<String, Set<Object>> destTbValidMap = new HashMap<>();
        for (Map<String, Object> map : destTbList) {
            for (String valid : tbValid) {
                Set<Object> set = destTbValidMap.get(valid);
                if (set == null) {
                    set = new HashSet<>();
                    destTbValidMap.put(valid, set);
                }
                set.add(map.get(valid));
            }
        }

        // 过滤掉校验属性在目标库中已存在的记录
        for (Map<String, Object> srcTbMap : srcTbList) {
            boolean flag = false;
            for (String valid : tbValid) {
                Object validValue = srcTbMap.get(valid);
                // 校验源库中指定待校验的属性值(validValue值)在目标库中是否存在
                Set<Object> set = destTbValidMap.get(valid);
                if (!set.contains(validValue)) {
                    set.add(validValue);
                } else {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                srcTbListCopy.add(srcTbMap);
            }
        }

        // 同步源数据到目标库中
        if (srcTbListCopy.size() > 0) {
            iUserInfoService.insertDestTables(tbName, srcTbListCopy);
        }
    }

    /**
     * 同步不需要校验的表数据
     *
     * @author Chen Yixing
     * @date 2020-11-18 15:29:47
     **/
    public void synchronizedLog(String tbName, List<String> date) {
        if (StringUtils.isEmpty(tbName)) {
            return;
        }

        // 查询源数据
        List<Map<String, Object>> srcTbList = iUserInfoService.listSrcTables(tbName, date);
        logger.info("源数据---> " + srcTbList);

        // 同步源数据
        if (srcTbList != null && srcTbList.size() > 0) {
            iUserInfoService.insertDestTables(tbName, srcTbList);
        }
    }
}
