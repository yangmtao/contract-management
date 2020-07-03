package com.bj.contract.controller;


import com.bj.common.enums.CommonEnum;
import com.bj.common.util.ExceptionUtil;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.contract.entity.ContractRisk;
import com.bj.contract.entity.SupplierEntity;
import com.bj.contract.service.ContractRiskService;
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
 * @author wgq
 * @since 2020-06-18
 */
@RestController
@RequestMapping("/contract/risk")
public class ContractRiskController extends AbstractController {
    @Autowired
    private ContractRiskService contractRiskService;

    /**
     * 分页列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("contract:risk:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = null;
        try {
            page = contractRiskService.queryPage(params);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("分页查询信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "分页查询信息出错");
        }

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("contract:risk:info")
    public R info(@PathVariable("id") Long id){
        System.out.println(id);
        ContractRisk contractRisk = contractRiskService.getById(id);
        System.out.println(contractRisk);

        return R.ok().put("contractRisk", contractRisk);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("contract:risk:update")
    public R update(@Validated @RequestBody ContractRisk contractRisk){
        try {

            return contractRiskService.saveRisk(contractRisk);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("修改信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "修改信息出错");
        }
    }

    /**
     * 解决该风险
     */
    @GetMapping("/over")
    public R over(Long id){
        contractRiskService.setDel(id);
        return R.ok();
    }

}

