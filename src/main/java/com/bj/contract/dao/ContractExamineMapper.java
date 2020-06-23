package com.bj.contract.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bj.contract.entity.ContractExamine;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-23
 */
public interface ContractExamineMapper extends BaseMapper<ContractExamine> {

    List<ContractExamine> queryAllContractExamine(Page page, @Param("ew") Wrapper wrapper);
}
