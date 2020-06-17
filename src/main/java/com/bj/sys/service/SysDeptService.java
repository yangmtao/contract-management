
package com.bj.sys.service;


import com.baomidou.mybatisplus.service.IService;
import com.bj.sys.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @author zhph
 * @date 2020-04-21 14:26:20
 */
public interface SysDeptService extends IService<SysDeptEntity> {

    List<SysDeptEntity> queryList(Map<String, Object> map);

    /**
     * 查询子部门ID列表
     *
     * @param parentId 上级部门ID
     */
    List<Long> queryDeptIdList(Long parentId);

    /**
     * 获取子部门ID，用于数据过滤
     */
    List<Long> getSubDeptIdList(Long deptId);

    void autoUpdate();

    /**
     * 查询正在使用的部门ID
     * @return
     * @throws Exception
     */
    List<Long> queryUserDeptIdList() throws Exception;
}
