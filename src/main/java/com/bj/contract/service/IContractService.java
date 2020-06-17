package com.bj.contract.service;

import com.baomidou.mybatisplus.service.IService;
import com.bj.common.util.PageUtils;
import com.bj.contract.entity.Contract;


import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-10
 */
public interface IContractService extends IService<Contract> {

    PageUtils queryPage(Map<String, Object> params) throws Exception;
}
