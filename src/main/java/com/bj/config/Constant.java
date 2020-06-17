package com.bj.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 常量配置类
 *
 * @author zhph
 */
@Component
@Data
public class Constant {
    /**
     * 文件存放目录
     */
    public static String fileAddress;

    /**
     * 文件访问路径--服务器
     */
    public static String serverAddress;

    /**
     * 文件访问路径--本项目
     */
    public static String localAddress;

    @Value("${system.fileAddress}")
    public void setFileAddress(String fileAddress) {
        Constant.fileAddress = fileAddress;
    }

    @Value("${system.serverAddress}")
    public void setServerAddr(String serverAddress) {
        Constant.serverAddress = serverAddress;
    }


    @Value("${system.localAddress}")
    public void setLocalAddr(String localAddress) {
        Constant.localAddress = localAddress;
    }



    /**
     * 用户加密使用的salt值
     */
    public static String pwdSalt;

    @Value("${user.pwd-salt}")
    public void setPwdSalt(String pwdSalt) {
        Constant.pwdSalt = pwdSalt;
    }


}
