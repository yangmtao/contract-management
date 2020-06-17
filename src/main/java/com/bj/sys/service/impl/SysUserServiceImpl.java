
package com.bj.sys.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bj.common.annotation.DataFilter;
import com.bj.common.enums.CommonEnum;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
import com.bj.sys.dao.SysUserDao;
import com.bj.sys.entity.SysUserEntity;
import com.bj.sys.service.SysDeptService;
import com.bj.sys.service.SysUserRoleService;
import com.bj.sys.service.SysUserService;
import com.bj.sys.shiro.ShiroUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


//系统用户
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	@DataFilter(subDept = true, user = false,deptId = "u.dept_id")
	public PageUtils queryPage(Map<String, Object> params) throws Exception {
		String userName = null!=params.get("userName") && StringUtils.isNotBlank(params.get("userName")+"")
				?(String)params.get("userName"):"";

		Page page = new Query<SysUserEntity>(params).getPage();
		Wrapper wrapper = SqlHelper.fillWrapper(page, new EntityWrapper<SysUserEntity>());

		if(StringUtils.isNotBlank(userName)){
			wrapper.gt("instr(user_name,'"+userName+"')", 0);
		}else{
			wrapper.eq("1",1);
		}
		wrapper.addFilterIfNeed(params.get(CommonEnum.SQL_FILTER) != null, (String)params.get(CommonEnum.SQL_FILTER)).orderBy("create_time",false);
		page.setRecords(baseMapper.queryAllUser(page,wrapper));
		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPwdSalt(salt);
		user.setUserPwd(ShiroUtils.sha256(user.getUserPwd(), user.getPwdSalt()));
		this.insert(user);
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getUserPwd())){
			user.setUserPwd(null);
		}else{
			user.setUserPwd(ShiroUtils.sha256(user.getUserPwd(), user.getPwdSalt()));
		}
		this.updateById(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}


	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setUserPwd(newPassword);
        return this.update(userEntity,
                new EntityWrapper<SysUserEntity>().eq("user_id", userId).eq("user_pwd", password));
    }

}
