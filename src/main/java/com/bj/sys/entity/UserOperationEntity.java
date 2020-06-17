package com.bj.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作记录
 *
 * @author zhph
 * @date 2020-04-21 14:26:20
 */
@ApiModel("用户操作记录表")
@TableName("sys_user_operation")
@Data
public class UserOperationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("操作记录ID")
    @TableId(value = "op_id", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long opId;

    @ApiModelProperty("用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty("用户ip")
    private String userIp;

    @ApiModelProperty("客户账号")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty("操作模块")
    private String opModule;

    @ApiModelProperty("操作内容")
    private String opContent;

    @ApiModelProperty("操作描述")
    private String opDesc;

    @ApiModelProperty("操作日期")
    private Date opDate;

    @ApiModelProperty("操作来源,1:前台,2:后台")
    private Integer opSource;

    @ApiModelProperty("使用平台")
    private Integer usePlatform;

}
