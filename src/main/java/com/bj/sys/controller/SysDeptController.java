
package com.bj.sys.controller;

import com.bj.common.enums.CommonEnum;
import com.bj.common.util.ExceptionUtil;
import com.bj.common.util.R;
import com.bj.sys.entity.SysDeptEntity;
import com.bj.sys.service.SysDeptService;
import com.bj.sys.shiro.ShiroUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;


/**
 * 部门管理
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public List<SysDeptEntity> list() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

        return deptList;
    }

    /**
     * 选择部门(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:dept:select")
    public R select() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

        //添加一级部门
//		if(getUserId() == CommonEnum.SUPER_ADMIN){
//			SysDeptEntity root = new SysDeptEntity();
//			root.setDeptId(0L);
//			root.setName("重庆水务集团");
//			root.setParentId(-1L);
//			root.setOpen(true);
//			deptList.add(root);
//		}

        return R.ok().put("deptList", deptList);
    }

    /**
     * 上级部门Id(管理员则为0)
     */
    @RequestMapping("/info")
    @RequiresPermissions("sys:dept:list")
    public R info() {
        long deptId = 0;
        if (getUserId() != CommonEnum.SUPER_ADMIN) {
            List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
            Long parentId = null;
            Long userDeptId = ShiroUtils.getUserEntity().getDeptId();
            for (SysDeptEntity sysDeptEntity : deptList) {
                if (userDeptId.longValue() == sysDeptEntity.getDeptId().longValue()) {
                    parentId = sysDeptEntity.getParentId();
                    break;
                }

//				if(parentId > sysDeptEntity.getParentId().longValue()){
//					parentId = sysDeptEntity.getParentId();
//				}
            }
            deptId = parentId;
        }

        return R.ok().put("deptId", deptId);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{deptId}")
    @RequiresPermissions("sys:dept:info")
    public R info(@PathVariable("deptId") Long deptId) {
        SysDeptEntity dept = sysDeptService.selectById(deptId);
        if (null != dept && dept.getParentId() != null) {
            SysDeptEntity parDept = sysDeptService.selectById(dept.getParentId());
            if (null != parDept && parDept.getDeptId() != null) {
                dept.setParentName(parDept.getDeptName());
            }
        }
        return R.ok().put("dept", dept);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:dept:save")
    public R save(@RequestBody SysDeptEntity dept) {
        sysDeptService.insert(dept);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:dept:update")
    public R update(@RequestBody SysDeptEntity dept) {
        sysDeptService.updateById(dept);

        return R.ok();
    }

    @RequestMapping("/refresh")
    @RequiresPermissions("sys:dept:refresh")
    public void refresh() {
        sysDeptService.autoUpdate();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dept:delete")
    public R delete(long deptId) {
        try {
            //当前登录用户所在部门的ID
            Long userDeptId = ShiroUtils.getUserEntity().getDeptId();
            if (userDeptId.longValue() == deptId || deptId == 10000L) {
                return R.error("所选部门不允许删除！");
            }
            boolean flag = true;
            List<Long> deptIdList = sysDeptService.queryUserDeptIdList();
            if (CollectionUtils.isNotEmpty(deptIdList)) {
                for (Long id : deptIdList) {
                    if (id.longValue() == deptId) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    return R.error("所选部门不允许删除!");
                }
            }
            //判断是否有子部门
            List<Long> deptList = sysDeptService.queryDeptIdList(deptId);
            if (deptList.size() > 0) {
                return R.error("请先删除子部门");
            }

            sysDeptService.deleteById(deptId);

            return R.ok();
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            return R.error("删除部门出错！"+msg);
        }
    }

}
