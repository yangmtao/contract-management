package com.bj.contract.controller;

import com.bj.common.util.R;
import com.bj.contract.dao.SupplierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

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
