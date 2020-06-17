/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bj.sys.controller;


import com.bj.common.annotation.UserLog;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.common.validator.Assert;
import com.bj.sys.entity.SysUserEntity;
import com.bj.sys.service.SysUserRoleService;
import com.bj.sys.service.SysUserService;
import com.bj.sys.shiro.ShiroUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) throws Exception {
        PageUtils page = sysUserService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @UserLog(remark = "修改密码")
    @RequestMapping("/password")
    public R password(String password, String newPassword) throws Exception {
        Assert.isBlank(newPassword, "新密码不为能空");

        //原密码
        password = ShiroUtils.sha256(password, getUser().getPwdSalt());
        //新密码
        newPassword = ShiroUtils.sha256(newPassword, getUser().getPwdSalt());

        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 修改系统用户密码
     */
    @UserLog(remark = "修改系统用户密码")
    @PostMapping("/modifyUserPwd")
    public R modifyUserPwd(String userId, String password, String newPassword) throws Exception {
        Assert.isBlank(newPassword, "新密码不为能空");

        //原密码
        password = ShiroUtils.sha256(password, getUser().getPwdSalt());
        //新密码
        newPassword = ShiroUtils.sha256(newPassword, getUser().getPwdSalt());
        //更新密码
        boolean flag = sysUserService.updatePassword(Long.valueOf(userId), password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }
        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public R info(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.selectById(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return R.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @UserLog(remark = "保存用户")
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody @Validated SysUserEntity user) {
        Map<String, Object> param = new HashMap<>();
        if (StringUtils.isBlank(user.getUserPwd())) {
            user.setUserPwd("123456");
        }
        sysUserService.save(user);
//		Long userId = user.getUserId();
//		if(userId != null){
//			List<String> areaIds = user.getAreaIds();
//			sysAreaService.delUserAreaByUserId(userId + "");
//			if(areaIds.size()>0) {
//				param.put("userId",userId);
//				param.put("list",areaIds);
//				sysAreaService.sysUserAreaInsertOrUpdate(param);
//			}
//		}
        return R.ok();
    }

    /**
     * 修改用户
     */
    @UserLog(remark = "修改用户")
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody @Validated SysUserEntity user) {
//		Map<String,Object> param = new HashMap<>();
//		Long userId = user.getUserId();
//		if(userId != null){
//			List<String> areaIds = user.getAreaIds();
//			sysAreaService.delUserAreaByUserId(userId + "");
//			if(areaIds.size()>0) {
//				param.put("userId",userId);
//				param.put("list",areaIds);
//				sysAreaService.sysUserAreaInsertOrUpdate(param);
//			}
//		}
        sysUserService.update(user);

        return R.ok();
    }

    /**
     * 删除用户
     */
    @UserLog(remark = "删除用户")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }

        sysUserService.deleteBatchIds(Arrays.asList(userIds));
        sysUserRoleService.deleteBatchByUser(userIds);

        return R.ok();
    }
}
