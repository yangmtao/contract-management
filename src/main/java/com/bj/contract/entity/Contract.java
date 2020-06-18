package com.bj.contract.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 合同表
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-09
 */
@Data
@Accessors(chain = true)
@TableName("contract")
public class Contract extends Model<Contract> {

    private static final long serialVersionUID = 1L;

    /**
     * 合同主键id
     */
    @TableId(value = "contract_id", type = IdType.AUTO)
    private Long contractId;
    /**
     * 合同名称
     */
    @TableField("contract_name")
    @NotBlank(message = "合同名称不能为空")
    @Size(min = 4,max = 100,message = "合同名称应在4~100字")
    private String contractName;
    /**
     * 经办人
     */
    @TableField("contract_manager")
    @NotNull(message = "必须选择经办人")
    private String contractManager;

    /**
     * 采购内容
     */
    @TableField("purchase_content")
    @NotBlank(message = "采购内容不能为空")
    private String purchaseContent;
    /**
     * 合同金额
     */
    @TableField("contract_amount")
    @DecimalMax(value = "1000000000",message = "合同金额不能大于10亿")
    @DecimalMin(value = "0",message = "合同金额不能小于0")
    private BigDecimal contractAmount;
    /**
     * 合同录入时间
     */
    @TableField(value = "create_date",fill= FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 开始时间
     */
    @TableField("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "合同开始时间不能为空")
   // @DateTimeFormat
    private Date startDate;
    /**
     * 结束时间
     */
    @TableField("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "合同结束时间不能为空")
    private Date endDate;
    /**
     * 采购部门id
     */
    @TableField("purchasing_dept_id")
    @NotNull(message = "必须选择采购部门")
    private Long purchasingDeptId;
    /**
     * 需求部门id
     */
    @TableField("demand_dept_id")
    @NotNull(message = "必须选择需求部门")
    private Long demandDeptId;
    /**
     * 合同类型
     */
    @TableField("contract_type")
    @NotNull(message = "必须选择合同类型")
    private Integer contractType;
    /**
     * 甲方公司id
     */
    @TableField("party_a_id")
    private Long partyAId;
    /**
     * 乙方公司id
     */
    @TableField("party_b_id")
    private Long partyBId;
    /**
     * 支付方式
     */
    @TableField("payment_type")
    private Integer paymentType;
    /**
     * 付款阶段
     */
    @TableField(exist = false)
    private String paymentStage;
    /**
     * 合同相关附件
     */
    @TableField("contract_file")
    private String contractFile;
    /**
     * 合同编号
     */
    @TableField("contract_code")
    private String contractCode;
    /**
     * 付款状态
     */
    @TableField("pay_status")
    private Integer payStatus;
    /**
     *
     * 逻辑删除
     */
    @TableField("del_tag")
    @TableLogic
    private Integer delTag;

    @Override
    protected Serializable pkVal() {
        return this.contractId;
    }

}
