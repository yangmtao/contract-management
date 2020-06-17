
package com.bj.sys.service;


import com.baomidou.mybatisplus.service.IService;
import com.bj.common.util.PageUtils;
import com.bj.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;


//角色
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params) throws Exception;

	List<SysRoleEntity> queryList(Map<String, Object> params);

	void save(SysRoleEntity role);

	void update(SysRoleEntity role);
	
	void deleteBatch(Long[] roleIds) throws Exception;

	List<Long> queryUserRoleIds() throws Exception;

}
