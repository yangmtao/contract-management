package com.bj.contract.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import com.bj.contract.entity.Contract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    Contract selectContractById(Long contractId);

    Contract selectContractDetail (@Param("contractId") Long contractId);

    List<Contract> queryAllContract(Page page, @Param("ew") Wrapper wrapper);

    List<Contract> selectContractByIds(List<String> ids);


    void updateAmount(@Param("contractId")Long contractId,@Param("o") Object o, @Param("unAmount") BigDecimal unAmount);

    Contract contractById(Long contractId);

    List<Contract> queryAll();

    String[] selectAllYear();

    List<Map<String,String>>  selectTypeCount(@Param("year") String year);

    List<Map<String, String>> selectTypeCountMonth(@Param("year") String year, @Param("type") Integer type);

    List<Map<String, String>> selectMonthNumberByYear(@Param("year") String year);

    List<Map<String, String>> selectAllYearAndCount();
    Long getRole(Long userId);
}
