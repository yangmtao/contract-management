package com.bj.contract.service;

import com.bj.common.util.PageUtils;
import com.bj.contract.entity.Contract;
import com.bj.contract.entity.ContractExamine;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-23
 */
public interface ContractExamineService extends IService<ContractExamine> {

    int save(ContractExamine examine);

    PageUtils queryPage(Map<String, Object> params) throws Exception;

}
