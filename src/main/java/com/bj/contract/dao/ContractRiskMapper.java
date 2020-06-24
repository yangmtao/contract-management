package com.bj.contract.dao;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bj.contract.entity.ContractRisk;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-18
 */
public interface ContractRiskMapper extends BaseMapper<ContractRisk> {

    boolean setDel(Long id);
    List<ContractRisk> queryRisk(Page<ContractRisk> page, @Param("ew") EntityWrapper<ContractRisk> riskWrapper);

    ContractRisk queryById(Long id);

    Integer save(ContractRisk contractRisk);

    ContractRisk select(Long contractId, String Risk);
}
