package com.sunt.qpschedule.config;

import com.sunt.qpschedule.QpApplication;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author Lihui
 * @date 2018/1/11 下午5:01
 */
public class SuntApplicationContext {
    private static Logger logger = LoggerFactory.getLogger(SuntApplicationContext.class);
    private static SuntApplicationContext s_Instance = new SuntApplicationContext();

    private ApplicationContext ctx;

    private SuntApplicationContext() {

    }

    public static SuntApplicationContext getInstance() {
        return s_Instance;
    }

    public <T> T getBeanInstance(Class<T> cls) {
        return ctx.getBean(cls);
    }

    public void initialize() {
        PropertyConfigurator.configure(QpApplication.class.getClassLoader().getResourceAsStream("log4j.properties"));
        try {
            ctx = new ClassPathXmlApplicationContext("classpath:spring-application.xml");
        } catch (Exception e) {
            logger.error("context is error" + e.getMessage());
        }

    }
}
