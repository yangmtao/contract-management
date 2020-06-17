package com.bj.cache;

import com.bj.ContractManagementApplication;
import com.bj.common.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * desc:初始化Application Context 等
 *
 * @author zhph
 * @date 2019/9/16  11:12
 */
@Component
public class InitData implements CommandLineRunner {
    private static Logger logger= LoggerFactory.getLogger(ContractManagementApplication.class);
    @Autowired
    WebApplicationContext webApplicationConnect;
    @Override
    public void run(String... args) throws Exception {
        logger.info("===========开始初始化工作=========");
        //初始化 Application Context
        ApplicationContextUtil.setApplicationContext(webApplicationConnect);
    }
}
