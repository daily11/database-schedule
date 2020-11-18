package com.sunt.qpschedule.config;

import com.sunt.qpschedule.QpApplication;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Chen Yixing
 * @date 2020/11/18 11:29
 *
 * 定义：获取所有配置文件信息的类
 */
@Component
public class Config {

    public ApplicationConfig getApplicationConfig() {
        Yaml yaml = new Yaml();
        return yaml.loadAs(QpApplication.class.getResourceAsStream("/application.yml"), ApplicationConfig.class);
    }
}
