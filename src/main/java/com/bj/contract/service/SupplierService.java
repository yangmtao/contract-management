package com.bj.contract.service;

import com.baomidou.mybatisplus.service.IService;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.contract.entity.SupplierEntity;

import java.util.List;
import java.util.Map;

/**
 * 供应商表
 *
 * @author contract
 * @email 1079626899@qq.com
 * @date 2020-06-08 10:41:06
 */
public interface SupplierService extends IService<SupplierEntity> {

    PageUtils queryPage(Map<String, Object> params) throws Exception;


   SupplierEntity getById(Long supplierId);


    R updateSupplier(SupplierEntity supplier);

    R saveSupplier(SupplierEntity supplier);

    R update(Long supplierId, Integer blackList, String remarks);

    List<Map<String, String>> selectSupplierSimple(String keyword);
}

