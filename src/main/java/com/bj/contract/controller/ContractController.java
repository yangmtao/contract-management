package com.bj.contract.controller;


import com.alibaba.fastjson.JSON;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.contract.entity.Contract;
import com.bj.contract.entity.ContractPaymentStage;
import com.bj.contract.service.ContractPaymentStageService;
import com.bj.contract.service.ContractService;

import com.bj.sys.controller.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 合同表 前端控制器
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-09
 */
@RestController
@RequestMapping("/contract")
@Slf4j
public class ContractController extends AbstractController {
    @Autowired
    private ContractService contractFileService;

    @Autowired
    private ContractPaymentStageService paymentStageService;

    /**
     * 根据合同id获取合同信息，会将合同表中的外键信息进行转换为具体数据
     */
    @RequestMapping("/contractInfo/{contractId}")
    @RequiresPermissions("contract:contractInfo")
    public R info(@PathVariable("contractId") Long contractId){
        System.out.println(contractId);
        Contract contract = contractFileService.getById(contractId);
        System.out.println(contract);
        return R.ok().put("contract", contract);
    }


    //根据合同id获取合同信息，仅包含合同表中的数据
    @GetMapping("/info/{id}")
    @ResponseBody
    @RequiresPermissions("contract:select")
    public Contract getContractInfoById(@PathVariable("id") Long id) {
        Contract contract=contractFileService.getContractInfoById(id);
        return contract;
    }

    /*根据合同id获取合同详细信息，包括会将合同表中的外键信息进行转换为具体数据
    *也包括付款阶段等相关信息
     */
    @GetMapping("/detail")
    @ResponseBody
    @RequiresPermissions("contract:select")
    public R getContractDetailById(@RequestParam("contractId") Long id) {
        Contract contract=contractFileService.getContractDetailById(id);
        Map<String,Object> queryMap=new HashMap<>();
        //selectByMap中的参数map的键是数据库字段，而不是实体属性
        queryMap.put("contract_id",id);
        List<ContractPaymentStage> paymentStages=paymentStageService.selectByMap(queryMap);

        return R.ok().put("contract",contract).put("paymentStages",paymentStages);
    }


    /**
     * 分页获取所有合同信息
     */
    @RequestMapping("/list")
    @ResponseBody
    @RequiresPermissions("contract:list")
    public R list(@RequestParam Map<String, Object> params) throws Exception {
        Long deptId = getDeptId();
        PageUtils page = null;
        //如果为系统管理员，可查看所有合同信息
        if(deptId==10000){
            page  = contractFileService.queryPage(params,null);
        }else {
            //仅可查看本部门合同信息
            page  = contractFileService.queryPage(params,deptId);
        }
        log.info("contract list");

        return R.ok().put("page", page);
    }



    //图片上传
    @RequestMapping("/uploadImage")
    @ResponseBody
    public R uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        log.info("image upload===============");
        //设置图片保存名称
        String imageName= UUID.randomUUID().toString()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //设置保存路径
        String pic_path ="D:\\image\\";
        System.out.println(pic_path);
        //保存到本地
        File targetFile=new File(pic_path,imageName);
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();
            targetFile.createNewFile();
        }
        file.transferTo(targetFile);
        String fileUrl = "http://localhost:9101/localImage/"+imageName;

        return R.ok().put("url",fileUrl).put("imageName",file.getOriginalFilename());
    }

    //保存、新增合同
    @PostMapping("/save")
    @ResponseBody
    @RequiresPermissions("contract:save")
    public R saveContract(@RequestBody Contract contractFile){
        System.out.println(JSON.toJSONString(contractFile));
        contractFile.setPayStatus(0);
        contractFile.setCreateDate(new Date());
        //保存合同信息到合同表
        boolean insert = contractFileService.insert(contractFile);

        if(insert){
            //保存付款阶段
            List<ContractPaymentStage> stages=JSON.parseArray(contractFile.getPaymentStage(),ContractPaymentStage.class);
            for(int i=0;i<stages.size();i++){
                stages.get(i).setContractId(contractFile.getContractId());
                paymentStageService.insert(stages.get(i));
            }
            return R.ok("录入成功");

        }else{
            return R.error("服务器故障，请联系管理员");
        }
    }

    //合同信息导出为excel
    @GetMapping("/export/excel")
    public R excelExport(@RequestParam("ids") String ids,HttpServletResponse response) throws IOException, IllegalAccessException {
        String[] idStr=ids.split(",");
        List<String> idList= Arrays.asList(idStr);
        contractFileService.excelExport(response,idList);
        return R.ok().put("msg","导出成功");
    }

    //获取当前合同的所有年份
    @GetMapping("/years")
    public R contractAllYear(){
        String[] years=contractFileService.getAllYear();
        return R.ok().put("years",years);
    }

    //获取当前合同的所有年份以及对应的合同数量
    @GetMapping("/year/count")
    public R contractAllYearAndCount(){
        List<Map<String,String>> years=contractFileService.getAllYearAndCount();
        return R.ok().put("yearAndCount",years);
    }

    //根据年份获取各个合同类别的合同数量
    @GetMapping("/type/numbers")
    public R getTypeNumbersByType(@RequestParam("year") String year){
        List<Map<String,String>> typeCount=  contractFileService.getTypeCount(year);
        return R.ok().put("typeCount",typeCount);
    }

    //根据年份获取各个合同类别每个月的合同数量
    @GetMapping("type/month/count")
    public R getTypeCountAndMonthByYear(@RequestParam("year") String year ,Integer type){
        List<Map<String,String>> typeCountMonth=contractFileService.getTypeCountMonth(year,type);
        return R.ok().put("typeCountMonth",typeCountMonth);
    }

    //根据年份获取每个月对应的合同数量
    @GetMapping("/month/number")
    public R getMonthNumberByYear(@RequestParam("year") String year){
        List<Map<String,String>> monthNumber=contractFileService.getMonthNumberByYear(year);
        return R.ok().put("monthNumber",monthNumber);
    }
}

