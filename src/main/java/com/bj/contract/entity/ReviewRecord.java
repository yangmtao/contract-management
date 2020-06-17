package com.bj.contract.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-10
 */
@Data
@Accessors(chain = true)
@TableName("contract_review_record")
public class ReviewRecord extends Model<ReviewRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 审查记录id
     */
    @TableId("review_record_id")
    private Long reviewRecordId;
    /**
     * 严重等级
     */
    @TableField("risk_level")
    private Integer riskLevel;
    /**
     * 修正状态
     */
    private Integer status;
    /**
     * 审查问题
     */
    @TableField("review_problem")
    private String reviewProblem;
    /**
     * 解决方案标题
     */
    @TableField("solution_title")
    private String solutionTitle;
    /**
     * 解决方案详情
     */
    @TableField("solution_detail")
    private String solutionDetail;
    /**
     * 合同id
     */
    @TableField("contract_id")
    private Long contractId;
    /**
     * 提出时间
     */
    @TableField("put_time")
    private Date putTime;
    /**
     * 提出人
     */
    private Long proposer;
    /**
     * 解决人
     */
    private Long solver;


    @Override
    protected Serializable pkVal() {
        return this.reviewRecordId;
    }

}
