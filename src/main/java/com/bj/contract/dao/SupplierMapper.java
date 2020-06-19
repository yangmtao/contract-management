package com.bj.contract.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.bj.contract.entity.SupplierEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 供应商表
 * 
 * @author contract
 * @email 1079626899@qq.com
 * @date 2020-06-08 10:41:06
 */
@Mapper
public interface SupplierMapper extends BaseMapper<SupplierEntity> {

    List<Map<String, String>> selectSupplierSimple(@Param("keyword") String keyword);

	
}
