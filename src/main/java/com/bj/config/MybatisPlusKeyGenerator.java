package com.bj.config;

import com.baomidou.mybatisplus.incrementer.IKeyGenerator;
import com.bj.common.util.SnowflakeIdWorker;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 配置主键生成策略
 * @author zhph
 */
@Component
@ConfigurationProperties(prefix = "user.key")
public class MybatisPlusKeyGenerator implements IKeyGenerator {

    public  Integer workerId;
    public  Integer dataCenterId;


    private  void setWorkerId(Integer workerId){
        this.workerId=workerId;
    }

    private  void setDataCenterId(Integer dataCenterId){
        this.dataCenterId=dataCenterId;
    }
    @Override
    public String executeSql(String incrementerName) {
        long uid = new SnowflakeIdWorker(workerId, dataCenterId).nextId();
        System.out.println("生成主键===="+uid);
        return "select " + uid + " from dual";
    }

}
