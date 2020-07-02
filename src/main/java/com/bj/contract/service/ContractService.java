package com.bj.contract.service;

import com.baomidou.mybatisplus.service.IService;
import com.bj.common.util.PageUtils;
import com.bj.contract.entity.Contract;


import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-10
 */
public interface ContractService extends IService<Contract> {

    PageUtils queryPage(Map<String, Object> params,Long deptId) throws Exception;

    Contract getContractInfoById(Long id);
    Contract getById(Long contractId);

    void excelExport(HttpServletResponse response, List<String> ids) throws FileNotFoundException, IOException, IllegalAccessException;

    Contract getContractDetailById(Long id);

    String[] getAllYear();

    List<Map<String,String>>  getTypeCount(String year);

    List<Map<String, String>> getTypeCountMonth(String year, Integer type);

    List<Map<String, String>> getMonthNumberByYear(String year);

    List<Map<String,String>> getAllYearAndCount();
}
