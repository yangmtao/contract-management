package com.bj.contract.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("contract_change")
public class ContractChange extends Model<ContractChange> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 变更名称
     */
    @TableField("change_name")
    private String changeName;
    /**
     * 变更原因
     */
    @TableField("change_reason")
    private String changeReason;
    /**
     * 经办人
     */
    @TableField("contract_manager")
    private Long contractManager;

    @TableField("contract_id")
    private Long contractId;
    /**
     * 合同变更内容
     */
    @TableField("contract_amount")
    private String contractAmount;
    /**
     * 合同变更录入时间
     */
    @TableField(value = "create_date",fill= FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    //支付时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("payment_date")
    private Date paymentDate;

    //合同变更原件
    @TableField("change_file")
    private String changeFile;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
