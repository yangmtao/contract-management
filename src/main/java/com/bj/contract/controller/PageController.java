package com.bj.contract.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {


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



}
