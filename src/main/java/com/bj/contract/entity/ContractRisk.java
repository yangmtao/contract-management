package com.bj.contract.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

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
 * @since 2020-06-18
 */
@Data
@Accessors(chain = true)
@TableName("contract_risk")
public class ContractRisk extends Model<ContractRisk> {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @TableField("contract_id")
    private Long contractId;
    @TableField("risk_type")
    private String riskType;
    @TableField("risk_name")
    private String riskName;
    private String solution;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
