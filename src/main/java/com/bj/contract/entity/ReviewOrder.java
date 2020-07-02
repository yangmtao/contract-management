package com.bj.contract.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-29
 */
@Component
@Data
@Accessors(chain = true)
@TableName("review_order")
public class ReviewOrder extends Model<ReviewOrder> {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @TableField("node_name")
    private String nodeName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
