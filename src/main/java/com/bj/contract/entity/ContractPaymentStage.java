package com.bj.contract.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

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
 * @since 2020-06-19
 */
@Data
@Accessors(chain = true)
@TableName("contract_payment_stage")
public class ContractPaymentStage extends Model<ContractPaymentStage> {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 合同id
     */
    @TableField("contract_id")
    private Long contractId;
    @TableField("stage_name")
    private String stageName;
    /**
     * 付款用途
     */
    private String uses;
    /**
     * 付款金额
     */
    @TableField("payment_amount")
    private BigDecimal paymentAmount;
    /**
     * 付款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("payment_date")
    private Date paymentDate;
    /**
     * 付款比率
     */
    @TableField("payment_rate")
    private BigDecimal paymentRate;
    /**
     * 付款状态；0，未付；1，在付；2，已付
     */
    @TableField("payment_status")
    private Integer paymentStatus;
    /**
     * 已支付金额
     */
    @TableField("paid_amount")
    private BigDecimal paidAmount;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
