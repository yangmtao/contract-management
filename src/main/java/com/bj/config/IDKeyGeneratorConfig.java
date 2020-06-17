package com.bj.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 自定义主键生成策略
 * @author zhph
 */
@Configuration
@ConfigurationProperties(prefix = "user.key")
public class IDKeyGeneratorConfig {

    private Integer workerId;
    private Integer dataCenterId;


    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Integer getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Integer dataCenterId) {
        this.dataCenterId = dataCenterId;
    }
}

