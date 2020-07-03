package com.bj.contract.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
import com.bj.common.util.R;
import com.bj.contract.dao.ContractMapper;
import com.bj.contract.entity.Contract;
import com.bj.contract.entity.ContractSettlement;
import com.bj.contract.dao.ContractSettlementMapper;
import com.bj.contract.entity.SupplierEntity;
import com.bj.contract.service.ContractSettlementService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.TemporalAmount;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wgq
 * @since 2020-06-17
 */
@Service
public class ContractSettlementServiceImpl extends ServiceImpl<ContractSettlementMapper, ContractSettlement> implements ContractSettlementService {

    @Autowired
    private ContractMapper contractMapper;

    /**
     * 分页查询
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception {
        EntityWrapper<Contract> settlementWrapper = new EntityWrapper<>();
        String contractName = null != params.get("contractName")
                && StringUtils.isNotBlank(params.get("contractName") + "") ? params.get("contractName") + "" : "";
        String contractCode = null != params.get("contractCode")
                && StringUtils.isNotBlank(params.get("contractCode") + "") ? params.get("contractCode") + "" : "";
        String supplierName = null != params.get("supplierName")
                && StringUtils.isNotBlank(params.get("supplierName") + "") ? params.get("supplierName") + "" : "";
        String contractManagerName = null != params.get("contractManagerName")
                && StringUtils.isNotBlank(params.get("contractManagerName") + "") ? params.get("contractManagerName") + "" : "";

        if (StringUtils.isNotBlank(contractName)) {
            settlementWrapper.gt("instr(contract_name,'" + contractName + "')", 0);
        }
        if (StringUtils.isNotBlank(contractCode)) {
            settlementWrapper.gt("instr(contract_code,'" + contractCode + "')", 0);
        }
        if (StringUtils.isNotBlank(supplierName)) {
            settlementWrapper.gt("instr(supplier_name,'" + supplierName + "')", 0);
        }
        if (StringUtils.isNotBlank(contractManagerName)) {
            settlementWrapper.gt("instr(real_name,'" + contractManagerName + "')", 0);
        }
       settlementWrapper.where("un_pay_amount!=0").or().where("pay_status!=2");
        Page<Contract> page = new Query<Contract>(params).getPage();
        page.setRecords(baseMapper.queryContract(page,settlementWrapper));

        return new PageUtils(page);
    }


    //新增结算
    @Override
    public R saveSettlement(ContractSettlement contractSettlement) {
        Contract contract = contractMapper.selectContractById(contractSettlement.getContractId());
        Long contractId = contract.getContractId();
        if (contract.getUnPayAmount().equals(BigDecimal.ZERO)){
            contractMapper.updateAmount(contractId,0,BigDecimal.ZERO);
        }else {
            BigDecimal amount = contract.getUnPayAmount();
            BigDecimal receiveAmount = contractSettlement.getReceiveAmount();
            BigDecimal unAmount = amount.subtract(receiveAmount);
            if (unAmount.equals(BigDecimal.ZERO)){
                contractMapper.updateAmount(contractId,0,BigDecimal.ZERO);
            }
            else {
                contractMapper.updateAmount(contractId,"",unAmount);
            }

        }


        baseMapper.insert(contractSettlement);
        return R.ok();
    }
}
