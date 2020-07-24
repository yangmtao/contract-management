package com.bj.contract.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2020-06-17
 */
@Data
@Accessors(chain = true)
@TableName("user_review_record")
public class UserReviewRecord extends Model<UserReviewRecord> {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
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

    /**
     * 合同录入时间
     */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 开始时间
     */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "合同开始时间不能为空")
    // @DateTimeFormat
    private Date startDate;
    /**
     * 结束时间
     */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "合同结束时间不能为空")
    private Date endDate;

    /**
     * 节点名称
     */
    @TableField(exist = false)
    private String nodeName;

    /**
     * 合同的id
     */
    @TableField("contract_id")
    private Long contractId;
    /**
     * 审核意见
     */
    @TableField("review_advise")
    private String reviewAdvise;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 审核结果
     */
    @TableField("review_result")
    private String reviewResult;
    /**
     * 审核的人
     */
    @TableField("reviewer")
    private String reviewer;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
