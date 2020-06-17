
package com.bj.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.bj.sys.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

//角色管理
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

    /**
     * 查询角色列表
     * @param var1
     * @param var2
     * @return
     */
    List<Map<String,Object>> queryAllRole(RowBounds var1, @Param("ew") Wrapper var2);

    /**
     * 查询正在使用的角色ID
     * @return
     */
    List<Long> queryUserRoleIds();

}
