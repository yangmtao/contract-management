package com.bj.contract.controller;


import com.bj.common.enums.CommonEnum;
import com.bj.common.util.ExceptionUtil;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.contract.entity.ContractRisk;
import com.bj.contract.service.ContractRiskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bj.sys.controller.AbstractController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-18
 */
@RestController
@RequestMapping("/contract/risk")
public class ContractRiskController extends AbstractController {
    @Autowired
    private ContractRiskService contractRiskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("contract:risk:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = null;
        try {
            page = contractRiskService.queryPage(params);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("分页查询供应商信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "分页查询供应商信息出错");
        }

        return R.ok().put("page", page);
    }

}

