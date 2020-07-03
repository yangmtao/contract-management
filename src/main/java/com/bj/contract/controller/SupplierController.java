package com.bj.contract.controller;

import com.bj.common.enums.CommonEnum;
import com.bj.common.util.ExceptionUtil;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.contract.entity.SupplierEntity;
import com.bj.contract.service.SupplierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 供应商表
 *
 * @author wgq
 * @email 1079626899@qq.com
 * @date 2020-06-08 10:41:06
 */
@RestController
@RequestMapping("contract/supplier")
public class SupplierController {
    private Logger logger = LoggerFactory.getLogger(SupplierController.class);
    @Autowired
    private SupplierService supplierService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("contract:supplier:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = null;
        try {
            page = supplierService.queryPage(params);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("分页查询供应商信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "分页查询供应商信息出错");
        }

        return R.ok().put("page", page);
    }

    //根据供应商名称关键词查询供应商id和完整名称
    @GetMapping("/simpleInfo")
    @ResponseBody
    public R supplierSimpleInfo(@RequestParam("keyword") String keyword) throws Exception {
        List<Map<String, String>> maps = supplierService.selectSupplierSimple(keyword);
        return R.ok().put("partyBs", maps);
    }

    //根据供应商id获取供应商名称
    @GetMapping("/name/{supplierId}")
    @ResponseBody
    public R supplierName(@PathVariable("supplierId") Long id) throws Exception {
        String supplierName=supplierService.getSupplierNameById(id);
        return R.ok().put("supplierName", supplierName);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{supplierId}")
    @RequiresPermissions("contract:supplier:info")
    public R info(@PathVariable("supplierId") Long supplierId){
        System.out.println(supplierId);
		SupplierEntity supplier = supplierService.getById(supplierId);
        System.out.println(supplier);

        return R.ok().put("supplier", supplier);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("contract:supplier:save")
    public R save(@Validated @RequestBody SupplierEntity supplier, BindingResult result){
        List<String> errors = new ArrayList<>();
        if (result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors){
                errors.add(error.getDefaultMessage());
            }
        }

        try {
            return supplierService.saveSupplier(supplier);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("新增信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "修改信息出错");
        }

    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("contract:supplier:update")
    public R update(@Validated @RequestBody SupplierEntity supplier){
        try {
            System.out.println(supplier+"------------");
            return supplierService.updateSupplier(supplier);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("修改信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "修改信息出错");
        }
    }

    /**
     * 逻辑删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("contract:supplier:delete")
    public R delete(@RequestBody Long[] supplierIds){

            supplierService.deleteBatchIds(Arrays.asList(supplierIds));

            return R.ok();

    }

    //黑名单
    @GetMapping("/move")
    public R move(Long supplierId, Integer blackList, String remarks){
        supplierService.update(supplierId,blackList,remarks);
        return R.ok();
    }

}
