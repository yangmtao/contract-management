package com.bj.contract.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import com.bj.contract.entity.Contract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合同表 Mapper 接口
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-09
 */
@Mapper
public interface ContractMapper extends BaseMapper<Contract> {

    List<Contract> queryAllContract(Page page, @Param("ew") Wrapper wrapper);

    List<Contract> selectContractByIds(List<String> ids);
}
