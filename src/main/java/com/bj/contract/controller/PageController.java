package com.bj.contract.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    @Autowired
    private ContractController contractController;
    //contract

    //合同录入页面
    @GetMapping("/contract/add.html")
    public String contract_add(){
        return "contract/contract_add";
    }

    //合同查询页面
    @GetMapping("/contract/query.html")
    public String contract_query(){
        return "contract/contract_query";
    }

    //合同审查记录页面
    @GetMapping("/contract/examine.html")
    public String contractExamine(){
        return "contract/contract_examine";
    }

    //合同更改页面
    @GetMapping("/contract/change.html")
    public String contractChange(){
        return "contract/contract_change";
    }

    //添加合同审查页面
    @GetMapping("/contract/examine/add.html")
    public String examineAdd(){
        return "contract/contract_examine_add";
    }


    //合同信息统计与分析页面
    @GetMapping("/contract/statistic.html")
    public String statistic(){
        return "contract/statistic";
    }


}
