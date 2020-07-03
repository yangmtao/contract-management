package com.bj.contract.controller;


import com.bj.common.enums.CommonEnum;
import com.bj.common.util.ExceptionUtil;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.contract.entity.ContractSettlement;
import com.bj.contract.service.ContractSettlementService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.bj.sys.controller.AbstractController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wgq
 * @since 2020-06-17
 */
@RestController
@RequestMapping("/contract/settlement")
public class ContractSettlementController extends AbstractController {

    @Autowired
    private ContractSettlementService contractSettlementService;
    /**
     * 分页列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("contract:settlement:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = null;
        try {
            page = contractSettlementService.queryPage(params);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("分页查询供应商信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "分页查询供应商信息出错");
        }
        return R.ok().put("page", page);
    }

    /**
     * 新增保存
     */
    @PostMapping("/save")
    @RequiresPermissions("contract:supplier:save")
    public R save(@Validated @RequestBody ContractSettlement contractSettlement){
        try {
            return contractSettlementService.saveSettlement(contractSettlement);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("修改信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "修改信息出错");
        }

    }
}

