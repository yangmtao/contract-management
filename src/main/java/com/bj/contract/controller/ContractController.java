package com.bj.contract.controller;


import com.alibaba.fastjson.JSON;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.contract.entity.Contract;
import com.bj.contract.service.IContractService;

import com.bj.sys.controller.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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
    private IContractService contractFileService;

    /**
     * 分页获取所有合同
     */
    @RequestMapping("/list")
    @ResponseBody
    @RequiresPermissions("contract:list")
    public R list(@RequestParam Map<String, Object> params) throws Exception {

        log.info("contract list");
        PageUtils page =page = contractFileService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{contractId}")
    @RequiresPermissions("contract:info")
    public R info(@PathVariable("contractId") Long contractId){
        System.out.println(contractId);
        Contract contract = contractFileService.getById(contractId);
        System.out.println(contract);

        return R.ok().put("contract", contract);
    }

    //图片上传
    @RequestMapping("/uploadImage")
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
        String fileUrl = "http://localhost:9001/localImage/"+imageName;

        return R.ok().put("url",fileUrl).put("imageName",file.getOriginalFilename());
    }

    //保存、新增合同
    @PostMapping("/save")
    //@RequiresPermissions("contract:save")
    public R saveContract(@RequestBody Contract contractFile){
        System.out.println(JSON.toJSONString(contractFile));
        contractFile.setPayStatus(0);
        boolean insert = contractFileService.insert(contractFile);
        if(insert){
            return R.ok("录入成功");
        }else{
            return R.error("服务器故障，请联系管理员");
        }
    }
}

