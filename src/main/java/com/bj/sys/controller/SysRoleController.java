
package com.bj.sys.controller;

import com.bj.common.annotation.UserLog;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.sys.entity.SysRoleEntity;
import com.bj.sys.service.SysRoleDeptService;
import com.bj.sys.service.SysRoleMenuService;
import com.bj.sys.service.SysRoleService;
import com.bj.sys.service.SysUserRoleService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 角色列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:role:list")
    public R list(@RequestParam Map<String, Object> params) throws Exception {
        PageUtils page = sysRoleService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 角色列表
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:role:select")
    public R select() {
        List<SysRoleEntity> list = sysRoleService.queryList(new HashMap<>());

        return R.ok().put("list", list);
    }

    /**
     * 角色信息
     */
    @RequestMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public R info(@PathVariable("roleId") Long roleId) {
        SysRoleEntity role = sysRoleService.selectById(roleId);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);

        //查询角色对应的部门
        List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[]{roleId});
        role.setDeptIdList(deptIdList);

        return R.ok().put("role", role);
    }

    /**
     * 保存角色
     */
    @UserLog(remark = "保存角色")
    @RequestMapping("/save")
    @RequiresPermissions("sys:role:save")
    public R save(@RequestBody @Validated SysRoleEntity role) {
        sysRoleService.save(role);

        return R.ok();
    }

    /**
     * 修改角色
     */
    @UserLog(remark = "修改角色")
    @RequestMapping("/update")
    @RequiresPermissions("sys:role:update")
    public R update(@RequestBody @Validated SysRoleEntity role) {
        sysRoleService.update(role);
        return R.ok();
    }

    /**
     * 删除角色
     */
    @UserLog(remark = "删除角色")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public R delete(@RequestBody Long[] roleIds) {
        try {
            List<Long> idList = sysRoleService.queryUserRoleIds();
            if (roleIds != null && roleIds.length > 0) {
                List<Long> roleIdList = new ArrayList<>();
                for (int i = 0; i < roleIds.length; i++) {
                    if (roleIds[i].longValue() != 10000L) {
                        boolean flag = true;
                        if (CollectionUtils.isNotEmpty(idList)) {
                            for (Long roleId : idList) {
                                if (roleId.longValue() == roleIds[i].longValue()) {
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        if (flag) {
                            roleIdList.add(roleIds[i]);
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(roleIdList)) {
                    Long[] deleteRoleIds = new Long[roleIdList.size()];
                    roleIdList.toArray(deleteRoleIds);
                    sysRoleService.deleteBatch(deleteRoleIds);
                } else {
                    return R.error("所选角色不允许删除！");
                }
            }
            return R.ok();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error(e.getMessage());
        }
    }
}
