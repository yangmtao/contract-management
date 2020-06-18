package com.bj.contract.service;

import com.bj.common.util.PageUtils;
import com.bj.contract.entity.ContractRisk;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-18
 */
public interface ContractRiskService extends IService<ContractRisk> {

    PageUtils queryPage(Map<String, Object> params) throws Exception;
}
