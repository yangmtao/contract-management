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
    @Id
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
    @TableField("operation_name")
    private String operationName;
    /**
     * 合同内容
     */
    @TableField("contract_content")
    private String contractContent;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
