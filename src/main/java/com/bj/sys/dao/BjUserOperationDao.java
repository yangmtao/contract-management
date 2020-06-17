package com.bj.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bj.sys.entity.UserOperationEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户操作记录表
 * 
 * @author zhph
 * @email 
 * @date 2019-07-15 14:03:47
 */
public interface BjUserOperationDao extends BaseMapper<UserOperationEntity> {
    /**
     * 分页查询后台用户操作记录信息
     * @param params
     * @return
     */
	List<UserOperationEntity> queryUserOperationPage(Map<String, Object> params);

    /**
     * 查询后台用户操作记录总数
     * @param params
     * @return
     */
	int queryUserOperationTotal(Map<String, Object> params);
	/**
     * 分页查询前台用户操作记录信息
     * @param params
     * @return
     */
	List<UserOperationEntity> queryUserOperationPageFront(Map<String, Object> params);

    /**
     * 查询前台用户操作记录总数
     * @param params
     * @return
     */
	int queryUserOperationTotalFront(Map<String, Object> params);
}
