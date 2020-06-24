
package com.bj.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.bj.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

//系统用户
public interface
SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 查询用户列表
	 * @param var1
	 * @param var2
	 * @return
	 */
	List<Map<String,Object>> queryAllUser(RowBounds var1, @Param("ew") Wrapper var2);

    List<Map<String, String>> selectUserDept(@Param("keyword") String keyword);

    String selectUserNameById(@Param("id") Long id);
}
