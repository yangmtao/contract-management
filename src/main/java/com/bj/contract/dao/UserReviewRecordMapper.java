package com.bj.contract.dao;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bj.contract.entity.Contract;
import com.bj.contract.entity.ContractSettlement;
import com.bj.contract.entity.UserReviewRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-17
 */
public interface UserReviewRecordMapper extends BaseMapper<UserReviewRecord> {

    void updateNode(@Param("contractId") Long contractId,@Param("a") int a);

    List<Contract> queryReview(Page<Contract> page,@Param("ew") EntityWrapper<ContractSettlement> reviewWrapper);
}
