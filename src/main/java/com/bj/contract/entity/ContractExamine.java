package com.bj.contract.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-23
 */
@Data
@Accessors(chain = true)
@TableName("contract_examine")
public class ContractExamine extends Model<ContractExamine> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 合同id
     */
    @TableField("contract_id")
    private Long contractId;

    //合同编号
    @TableField(exist = false)
    private String contractCode;

    //合同名称
    @TableField(exist = false)
    private String contractName;

    //合同经办人
    @TableField(exist = false)
    private String contractManagerName;
    /**
     * 合同审查风险等级；
     */
    @TableField("risk_level")
    private String riskLevel;
    /**
     * 审查问题
     */
    private String problem;
    /**
     * 审查问题修正状态；0，待处理；1，处理中；2，已解决
     */
    private Integer status;
    /**
     * 解决方案
     */
    @TableField("handle_way")
    private String handleWay;
    /**
     * 提出时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 提出人
     */
    private Long handler;
    //提出人姓名
    @TableField(exist = false)
    private String handlerName;





    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
