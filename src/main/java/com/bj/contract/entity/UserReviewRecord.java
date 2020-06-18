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
@TableName("user_review_record")
public class UserReviewRecord extends Model<UserReviewRecord> {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @Id
    private Long id;
    /**
     * 合同的id
     */
    @TableField("contarct_id")
    private Long contarctId;
    /**
     * 审核意见
     */
    @TableField("review_advise")
    private String reviewAdvise;
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
