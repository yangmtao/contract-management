package com.bj.contract.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bj.common.enums.CommonEnum;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
import com.bj.common.util.R;
import com.bj.contract.dao.UserReviewRecordMapper;
import com.bj.contract.entity.ContractRisk;
import com.bj.contract.dao.ContractRiskMapper;
import com.bj.contract.entity.UserReviewRecord;
import com.bj.contract.service.ContractRiskService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-18
 */
@Service
public class ContractRiskServiceImpl extends ServiceImpl<ContractRiskMapper, ContractRisk> implements ContractRiskService {
//    @Autowired
//    private ContractRiskMapper contractRiskMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception {
        EntityWrapper<ContractRisk> riskWrapper = new EntityWrapper<>();

        String contractName = null != params.get("contractName")
                && StringUtils.isNotBlank(params.get("contractName") + "") ? params.get("contractName") + "" : "";
        String contractCode = null != params.get("contractCode")
                && StringUtils.isNotBlank(params.get("contractCode") + "") ? params.get("contractCode") + "" : "";
        String supplierName = null != params.get("supplierName")
                && StringUtils.isNotBlank(params.get("supplierName") + "") ? params.get("supplierName") + "" : "";
        String payStatus = null != params.get("payStatus")
                && StringUtils.isNotBlank(params.get("payStatus") + "") ? params.get("payStatus") + "" : "";

        if (StringUtils.isNotBlank(contractName)) {
            riskWrapper.gt("instr(contract_name,'" + contractName + "')", 0);
        }
        if (StringUtils.isNotBlank(contractCode)) {
            riskWrapper.gt("instr(contract_code,'" + contractCode + "')", 0);
        }
        if (StringUtils.isNotBlank(supplierName)) {
            riskWrapper.gt("instr(supplier_name,'" + supplierName + "')", 0);
        }
        if (StringUtils.isNotBlank(payStatus)) {
            riskWrapper.gt("instr(pay_status,'" + payStatus + "')", 0);
        }
        riskWrapper.orderBy("contract_name", false);

        riskWrapper.where("r.del=0");
        Page<ContractRisk> page = new Query<ContractRisk>(params).getPage();
        page.setRecords(baseMapper.queryRisk(page,riskWrapper));

        return new PageUtils(page);
    }


    //修改解决字段
    @Override
    public boolean setDel(Long id) {
        baseMapper.setDel(id);
        return true;
    }

    //根据id获取实体
    @Override
    public ContractRisk getById(Long id) {
        EntityWrapper<ContractRisk> riskWrapper = new EntityWrapper<>();
        ContractRisk contractRisk = baseMapper.queryById(id);
        return contractRisk;
    }


    /**
     * 修改风险信息
     * @param contractRisk
     * @return
     */
    @Override
    public R saveRisk(ContractRisk contractRisk) {
        if (contractRisk.getId() == null) {
            return R.error(CommonEnum.ReturnCode.PARAM.getValue(), "缺少参数[userId]！");
        }
        System.out.println(contractRisk.getId());
        ContractRisk contractRisk1 = baseMapper.selectById(contractRisk.getId());
        System.out.println(contractRisk1);
        if (contractRisk1 == null) {
            return R.error(CommonEnum.ReturnCode.PARAM.getValue(), "未获取到客户信息！");
        }
        contractRisk1.setRiskName(contractRisk.getRiskName());
        contractRisk1.setRiskType(contractRisk.getRiskType());
        contractRisk1.setSolution(contractRisk.getSolution());

        baseMapper.updateById(contractRisk1);
        return R.ok();
    }
}
