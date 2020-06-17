package com.bj.sys.controller;

import com.bj.common.enums.CommonEnum;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.sys.entity.UserOperationEntity;
import com.bj.sys.service.BjUserOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 用户业务操作记录表
 *
 * @author zhphz
 * @email
 * @date 2019-07-15 14:03:47
 */
@RestController
@RequestMapping("sys/bjuseroperation")
@Api(description = "用户业务操作记录表")
public class UserOperationController {
    private Logger logger = LoggerFactory.getLogger(UserOperationController.class);
    @Autowired
    private BjUserOperationService bjUserOperationService;

    /**
     * 分页查询后台用户业务操作记录
     *
     * @param params
     * @return
     */
    @RequestMapping("/backList")
    @RequiresPermissions("sys:bjuseroperation:list")
    @ApiOperation("分页查询后台用户业务操作记录")
    public R backList(@RequestParam Map<String, Object> params) {
        try {
            PageUtils page = bjUserOperationService.queryPageBySql(params);

            return R.ok().put("page", page);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "获取用户业务操作记录失败！");

        }
    }

    /**
     * 分页查询前台用户业务操作记录
     *
     * @param params
     * @return
     *   PageUtils page = bjUserOperationService.queryPageBySqlFront(params);
     */
    @RequestMapping("/frontList")
    @RequiresPermissions("sys:bjuseroperation:list")
    @ApiOperation("分页查询前台用户业务操作记录")
    public R frontList(@RequestParam Map<String, Object> params) {
        try {
            String opType = params.get("opType") != null ? params.get("opType") + "" : "";
            if(StringUtils.isBlank(opType)){
                params.put("opType",1);
            }
            PageUtils page = bjUserOperationService.queryPageFromMongoDb(params);

            return R.ok().put("page", page);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "获取用户业务操作记录失败！");

        }
    }

    /**
     * 获取用户业务操作详细信息
     *
     * @param opId
     * @return
     */
    @RequestMapping("/info/{opId}")
    @RequiresPermissions("sys:bjuseroperation:info")
    @ApiOperation("获取用户业务操作记录")
    public R info(@PathVariable("opId") Long opId) {
        UserOperationEntity bjUserOperation = bjUserOperationService.selectById(opId);

        return R.ok().put("bjUserOperation", bjUserOperation);
    }

    /**
     * 保存用户操作记录
     *
     * @param bjUserOperation
     * @return
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:bjuseroperation:save")
    @ApiOperation("新增用户操作记录")
    public R save(@Validated @RequestBody UserOperationEntity bjUserOperation) {
        bjUserOperationService.insert(bjUserOperation);

        return R.ok();
    }

    /**
     * 修改用户操作记录
     *
     * @param bjUserOperation
     * @return
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:bjuseroperation:update")
    @ApiOperation("修改用户操作记录")
    public R update(@Validated @RequestBody UserOperationEntity bjUserOperation) {

        bjUserOperationService.updateAllColumnById(bjUserOperation);

        return R.ok();
    }

    /**
     * 删除用户操作记录
     *
     * @param opIds
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:bjuseroperation:delete")
    @ApiOperation("删除用户操作记录")
    public R delete(@RequestBody Long[] opIds) {
        bjUserOperationService.deleteBatchIds(Arrays.asList(opIds));

        return R.ok();
    }

}
