package com.bj.contract.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-17
 */
@Data
@Accessors(chain = true)
@TableName("contract_settlement")
public class ContractSettlement extends Model<ContractSettlement> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 合同id
     */
    @TableField("contract_id")
    private Long contractId;
    /**
     * 收款银行
     */
    @TableField("receive_bank")
    private String receiveBank;
    /**
     * 收款账户
     */
    @TableField("receive_account")
    private String receiveAccount;
    /**
     * 收款单位
     */
    @TableField("receive_company")
    private String receiveCompany;
    /**
     * 收款阶段
     */
    @TableField("receive_stage")
    private String receiveStage;
    /**
     * 收款金额
     */
    @TableField("receive_amount")
    private BigDecimal receiveAmount;
    /**
     * 收款时间
     */
    @TableField("receive_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
