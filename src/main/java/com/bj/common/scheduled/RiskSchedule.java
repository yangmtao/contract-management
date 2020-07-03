package com.bj.common.scheduled;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bj.contract.dao.ContractRiskMapper;
import com.bj.contract.entity.Contract;
import com.bj.contract.entity.ContractRisk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.bj.contract.dao.ContractMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class RiskSchedule {
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private ContractRiskMapper contractRiskMapper;

    /**
     * 检测是否逾期
     */
    @Scheduled(cron = "0 * * * * ?")
    public void cron() {
        Date now= new Date();
        List<Contract> contracts = contractMapper.queryAll();

        for (int i = 0; i < contracts.size(); i++) {
            Date endDate = contracts.get(i).getEndDate();
            BigDecimal unPayAmount = contracts.get(i).getUnPayAmount();
            if(now.getTime()<endDate.getTime() && (!unPayAmount.equals(BigDecimal.ZERO))){
                ContractRisk contractRisk = new ContractRisk();
                contractRisk.setContractId(contracts.get(i).getContractId());
                Integer save=0;
                if (endDate.getTime()-now.getTime()<24*60*60*1000){
                    contractRisk.setRiskType("低");
                    contractRisk.setRiskName("未按时付款");
                    contractRisk.setSolution("打电话催款");
                    if (contractRiskMapper.select(contracts.get(i).getContractId(),"低")==null){
                        save = contractRiskMapper.save(contractRisk);
                    }
                }else if (endDate.getTime()-now.getTime()>=24*60*60*1000&&now.getTime()-endDate.getTime()<7*24*60*60*1000){
                    contractRisk.setRiskType("中");
                    contractRisk.setRiskName("未按时付款超过一天");
                    contractRisk.setSolution("上报领导，立即催款");
                    if (contractRiskMapper.select(contracts.get(i).getContractId(),"中")==null){
                        save = contractRiskMapper.save(contractRisk);
                    }
                }else if (endDate.getTime()-now.getTime()>7*24*60*60*1000){
                    contractRisk.setRiskType("高");
                    contractRisk.setRiskName("未按时付款超过七天");
                    contractRisk.setSolution("上报领导，前去公司催款");
                    if (contractRiskMapper.select(contracts.get(i).getContractId(),"高")==null){
                        save = contractRiskMapper.save(contractRisk);
                    }
                }

                if (save == 1){
                    log.info("添加风险成功");
                }else{
                    log.info("已存在");
                }

            }

        }
    }
}
