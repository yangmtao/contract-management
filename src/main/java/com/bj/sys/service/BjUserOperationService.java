package com.bj.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.bj.common.util.PageUtils;
import com.bj.sys.entity.UserOperationEntity;

import java.util.Map;

/**
 * 用户操作记录表
 *
 * @author zhph
 * @email
 * @date 2020-04-21 14:26:20
 */
public interface BjUserOperationService extends IService<UserOperationEntity> {
    /**
     * 分页查询用户操作记录（自动生成）
     *
     * @param params
     * @return
     * @throws Exception
     */
    PageUtils queryPage(Map<String, Object> params) throws Exception;

    /**
     * 分页查询后台用户操作记录（sql）
     *
     * @param params
     * @return
     * @throws Exception
     */
    PageUtils queryPageBySql(Map<String, Object> params) throws Exception;

    /**
     * 从MongoDB中分页查询用户的操作日志信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    PageUtils queryPageFromMongoDb(Map<String, Object> params) throws Exception;

    /**
     * 分页查询前台用户操作记录（sql）
     *
     * @param params
     * @return
     * @throws Exception
     */
    PageUtils queryPageBySqlFront(Map<String, Object> params) throws Exception;
}

