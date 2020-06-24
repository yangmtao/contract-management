package com.bj.contract.entity;

import com.baomidou.mybatisplus.annotations.*;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-18
 */
@Data
@Accessors(chain = true)
@TableName("contract_risk")
public class ContractRisk extends Model<ContractRisk> {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;


    /**
     * 合同名称
     */
    @TableField(exist = false)
    @NotBlank(message = "合同名称不能为空")
    @Size(min = 4,max = 100,message = "合同名称应在4~100字")
    private String contractName;
    /**
     * 合同金额
     */
    @TableField(exist = false)
    @DecimalMax(value = "1000000000",message = "合同金额不能大于10亿")
    @DecimalMin(value = "0",message = "合同金额不能小于0")
    private BigDecimal contractAmount;

    /**
     * 合同类型
     */
    @TableField(exist = false)
    @NotNull(message = "必须选择合同类型")
    private Integer contractType;

    /**
     * 乙方公司id
     */
    @TableField(exist = false)
    private Long partyBId;

    /**
     * 乙方名称
     */
    @TableField(exist = false)
    private String supplierName;

    /**
     * 支付方式
     */
    @TableField(exist = false)
    private Integer paymentType;

    /**
     * 付款阶段
     */
    @TableField(exist = false)
    private String paymentStage;

    /**
     * 合同编号
     */
    @TableField(exist = false)
    private String contractCode;

    /**
     * 付款状态
     */
    @TableField(exist = false)
    private Integer payStatus;

    @TableField("contract_id")
    private Long contractId;
    @TableField("risk_type")
    private String riskType;
    @TableField("risk_name")
    private String riskName;
    private String solution;

    /**
     *
     * 逻辑删除
     */
    @TableField("del")
    private Integer del;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
