package com.bj.contract.controller;

import com.bj.contract.entity.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @Autowired
    private ContractController contractController;
    //contract
    @GetMapping("/contract/add.html")
    public String contract_add(){
        return "contract/contract_add";
    }

    @GetMapping("/contract/query.html")
    public String contract_query(){
        return "contract/contract_query";
    }

    @GetMapping("/contract/examine.html")
    public String contractExamine(){
        return "contract/contract_examine";
    }

    @GetMapping("/contract/change.html")
    public String contractChange(){
        return "contract/contract_change";
    }

    @GetMapping("/contract/examine/add.html")
    public String examineAdd(){
        return "contract/contract_examine_add";
    }

    @GetMapping("/contract/{id}/detail.html")
    public String contractChange(@PathVariable("id") Long id, ModelMap modelMap){
        Contract contract = contractController.getContractInfoById(id);
        modelMap.put("contract",contract);
        return "contract/contract_detail";
    }

    @GetMapping("/contract/statistic.html")
    public String statistic(){
        return "contract/statistic";
    }


}
