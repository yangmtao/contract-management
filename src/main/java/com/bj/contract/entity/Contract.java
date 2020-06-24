package com.bj.contract.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
@ApiModel("合同信息")
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
     * 合同编号
     */
    @TableField("contract_code")
    @ApiModelProperty("合同编号")
    private String contractCode;
    /**
     * 合同名称
     */
    @ApiModelProperty("合同名称")
    @TableField("contract_name")
    @NotBlank(message = "合同名称不能为空")
    @Size(min = 4,max = 100,message = "合同名称应在4~100字")
    private String contractName;
    /**
     * 经办人id
     */
    @TableField("contract_manager")
    @NotNull(message = "必须选择经办人")
    private String contractManager;

    /**
     * 经办人
     */
    @TableField(exist = false)
    private String contractManagerName;

    /**
     * 采购内容
     */
    @TableField("purchase_content")
    @ApiModelProperty("合同内容")
    @NotBlank(message = "采购内容不能为空")
    private String purchaseContent;
    /**
     * 经办人真实姓名
     */
    @ApiModelProperty("合同经办人")
    @TableField(exist = false)
    private String contractManagerName;
    /**
     * 合同金额
     */
    @TableField("contract_amount")
    @ApiModelProperty("合同金额")
    @DecimalMax(value = "1000000000",message = "合同金额不能大于10亿")
    @DecimalMin(value = "0",message = "合同金额不能小于0")
    private BigDecimal contractAmount;

    /*
    合同金额范围
    合同查询时使用
     */
    @TableField(exist = false)
    private String paymentRange;
    /**
     * 合同录入时间
     */
    @TableField(value = "create_date",fill= FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("合同录入时间")
    private Date createDate;
    /**
     * 开始时间
     */
    @TableField("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "合同开始时间不能为空")
    @ApiModelProperty("开始时间")
    private Date startDate;
    /**
     * 结束时间
     */
    @TableField("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "合同结束时间不能为空")
    @ApiModelProperty("结束时间")
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
     * 需求部门
     */
    @TableField(exist = false)
    private String demandDeptName;
    /**
     * 需求部门名称
     */
    @TableField(exist = false)
    @ApiModelProperty("需求部门")
    private String demandDeptName;
    /**
     * 合同类型
     */
    @TableField("contract_type")
    @NotNull(message = "必须选择合同类型")
    private Integer contractType;
    /**
     * 合同类型名称
     */
    @TableField(exist = false)
    @ApiModelProperty("合同类型")
    private String contractTypeName;
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
     * 合同节点id
     */
    @TableField("contract_node")
    private Integer contractNode;

    /**
     * 节点名称
     */
    @TableField(exist = false)
    private String nodeName;


    /**
     * 乙方公司名称
     */
    @TableField(exist = false)
    @ApiModelProperty("乙方公司")
    private String partyBName;
    /**
     * 支付方式
     */
    @TableField("payment_type")
    @NotNull(message = "支付方式不能为空")
    private Integer paymentType;
    /**
     * 支付方式名称
     */
    @TableField(exist = false)
    @ApiModelProperty("支付方式")
    private String paymentTypeName;
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
     * 付款状态
     */
    @TableField("pay_status")
    private Integer payStatus;

    /**
     * 乙方名称
     */
    @TableField(exist = false)
    private String supplierName;
    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String purchasingDeptName;

    /**
     * 未付金额
     */
    @TableField("un_pay_amount")
    @DecimalMax(value = "1000000000",message = "合同金额不能大于10亿")
    @DecimalMin(value = "0",message = "未付金额不能小于0")
    private BigDecimal unPayAmount;
    /**
     * 付款状态名称
     */
    @TableField(exist = false)
    @ApiModelProperty("付款状态")
    private String payStatusName;
    /**
     *
     * 逻辑删除
     */
    @TableField("del_tag")
    @TableLogic
    private Integer delTag;

    /*
    合同变更次数，合同变更查询时使用
     */
    @TableField(exist = false)
    private int changeTimes;
    /*
    审查记录数，合同审查查询时使用
     */
    @TableField(exist = false)
    private int examineNumbers;

    @Override
    protected Serializable pkVal() {
        return this.contractId;
    }

}
