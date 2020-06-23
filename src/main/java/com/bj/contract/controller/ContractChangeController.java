package com.bj.contract.controller;


import com.bj.common.util.R;
import com.bj.contract.entity.ContractChange;
import com.bj.contract.service.ContractChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.bj.sys.controller.AbstractController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-17
 */
@RestController
@RequestMapping("/contract/change")
public class ContractChangeController extends AbstractController {
    @Autowired
    private ContractChangeService contractChangeService;
    @PostMapping("/add")
    public R addContractChange(@RequestBody ContractChange change){
        int i=contractChangeService.save(change);
        if(i==1){
            return R.ok();
        }
        return R.error();
    }

}

