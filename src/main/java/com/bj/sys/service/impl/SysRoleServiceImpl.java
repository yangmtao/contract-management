
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
import com.bj.sys.dao.SysRoleDao;
import com.bj.sys.entity.SysRoleEntity;
import com.bj.sys.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDeptService sysDeptService;

    @Override
    @DataFilter(subDept = true, user = false, deptId = "r.dept_id")
    public PageUtils queryPage(Map<String, Object> params) throws Exception {
        String roleName = null != params.get("roleName") &&
                StringUtils.isNotBlank(params.get("roleName") + "") ? (String) params.get("roleName") : "";
        Page page = new Query<SysRoleEntity>(params).getPage();
        Wrapper wrapper = SqlHelper.fillWrapper(page, new EntityWrapper<SysRoleEntity>());
        if (StringUtils.isNotBlank(roleName)) {
			wrapper.gt(" instr(role_name,'"+roleName+"')",0);
        }else{
            wrapper.eq("1", 1);
        }
        wrapper.addFilterIfNeed(params.get(CommonEnum.SQL_FILTER) != null, (String) params.get(CommonEnum.SQL_FILTER)).orderBy("create_time", false);
        page.setRecords(baseMapper.queryAllRole(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<Long> queryUserRoleIds() throws Exception {
        return baseMapper.queryUserRoleIds();
    }

    @Override
    @DataFilter(subDept = true, user = false)
    public List<SysRoleEntity> queryList(Map<String, Object> params) {
        Wrapper<SysRoleEntity> wrapper = new EntityWrapper<>();
        wrapper.addFilterIfNeed(params.get(CommonEnum.SQL_FILTER) != null, (String) params.get(CommonEnum.SQL_FILTER));
        return this.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysRoleEntity role) {
        role.setCreateTime(new Date());
        this.insert(role);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

        //保存角色与部门关系
        sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleEntity role) {
        this.updateAllColumnById(role);

        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

        //保存角色与部门关系
        sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) throws Exception {
        //删除角色
        this.deleteBatchIds(Arrays.asList(roleIds));

        //删除角色与用户关联
        int i = sysUserRoleService.deleteBatch(roleIds);
        if (i > 0) {
            throw new Exception("使用中，无法删除。");
        }

        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);

        //删除角色与部门关联
        sysRoleDeptService.deleteBatch(roleIds);

    }


}
