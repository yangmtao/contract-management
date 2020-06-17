package com.bj.sys.form;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * mongodb 中日志存储格式
 *
 * @author hmx
 * @version 1.0
 * @createDate 2020/04/02 14:31
 */
@Data
@Document
public class LogForm implements Serializable {
    /**
     * 对象唯一标识id
     */
    @Id
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 操作描述
     */
    private String operationDesc;
    /**
     * 方法类和方法名
     */
    private String methodDesc;

    /**
     * 操作耗时 ms
     */
    private Long opTime;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 请求ip
     */
    private String requestIp;
    /**
     * 所属平台
     */
    private Integer platform;
    /**
     * 企业名称
     */
    private String entName;
    /**
     * 企业id
     */
    private String entId;
    /**
     * 请求数据
     */
    private String requestData;

    /**
     * 备注
     */
    private String remark;
    /**
     * 操作分类，1：用户操作，2：系统请求
     */
    private Integer opType;

}
