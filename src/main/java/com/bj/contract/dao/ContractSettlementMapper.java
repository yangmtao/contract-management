package com.bj.contract.dao;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bj.contract.entity.Contract;
import com.bj.contract.entity.ContractSettlement;
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
public interface ContractSettlementMapper extends BaseMapper<ContractSettlement> {

    List<Contract> queryContract(Page<Contract> page,@Param("ew") EntityWrapper<Contract> settlementWrapper);
}
