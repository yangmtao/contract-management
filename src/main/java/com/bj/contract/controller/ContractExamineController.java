package com.bj.contract.controller;


import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.contract.entity.ContractExamine;
import com.bj.contract.service.ContractExamineService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bj.sys.controller.AbstractController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-23
 */
@RestController
@RequestMapping("/contract/examine")
public class ContractExamineController extends AbstractController {
    @Autowired
    private ContractExamineService examineService;

    /**
     * 分页获取所有合同审查记录
     */
    @RequestMapping("/list")
    @RequiresPermissions("contract:examine:list")
    public R list(@RequestParam Map<String, Object> params) throws Exception {

        PageUtils page  = examineService.queryPage(params);
        return R.ok().put("page", page);
    }

    @PostMapping("/add")
    @RequiresPermissions("contract:examine:add")
    public R examineAdd(@RequestBody  ContractExamine examine){
        int i=examineService.save(examine);
        if(i==1){
            return R.ok();
        }
        return R.error();
    }

    @GetMapping("/delete/{id}")
    @RequiresPermissions("contract:examine:delete")
    public R deleteExamine(@PathVariable("id") String id){
        boolean b = examineService.deleteById(Long.parseLong(id));
        if(b){
            return R.ok();
        }
        return R.error();
    }



}

