package com.bj.contract.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SupplierDao {
    List<Map<String,String>> selectSupplierSimple(@Param("keyword") String keyword);
}
