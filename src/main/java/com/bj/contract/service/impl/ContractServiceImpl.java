package com.bj.contract.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bj.common.enums.CommonEnum;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
import com.bj.contract.dao.ContractMapper;
import com.bj.contract.entity.Contract;
import com.bj.contract.service.IContractService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 合同表 服务实现类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-09
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception {
        Page page=new Query<Contract>(params).getPage();
        Wrapper wrapper= SqlHelper.fillWrapper(page,new EntityWrapper<Contract>());

        //合同名称
        String contractName = params.get("contractName")!=null && StringUtils.isNoneBlank(params.get("contractName")+"") ? (String)params.get("contractName") : "";
        String contractManagerName = params.get("contractManagerName")!=null
                && StringUtils.isNoneBlank(params.get("contractManagerName")+"") ? (String)params.get("contractManagerName") : "";

        String partyBId = null != params.get("partyBId")
                && StringUtils.isNotBlank(params.get("partyBId") + "") ? params.get("partyBId") + "" : "";

      /*  //状态
        String status=params.get("status")!=null &&StringUtils.isNotBlank(params.get("status")+"") ? (String)params.get("status") : "-1";
        //排序列
        String sidx=params.get("sidx")!=null && StringUtils.isNoneBlank(params.get("sidx")+"") ? (String)params.get("sidx") : "";
        //排序方式
        String order=params.get("order")!=null && StringUtils.isNoneBlank(params.get("order")+"") ? (String)params.get("order") : "";

*/

        //构造sql
        if(StringUtils.isNotBlank(contractName)){
            wrapper.gt("instr(contract_name,'"+contractName+"')",0);
        }else{
            wrapper.eq("1",1);
        }
        if(StringUtils.isNotBlank(contractManagerName)){
            wrapper.gt("instr(real_name,'"+contractManagerName+"')",0);
        }else{
            wrapper.eq("1",1);
        }

        if (StringUtils.isNotBlank(partyBId)) {
            wrapper.eq("party_b_id",Integer.parseInt(partyBId));
        }
       /* if(userId!=null){
            wrapper.and().eq("user_id",userId);
        }
        if(Integer.parseInt(status)!=-1){
            wrapper.and().eq("status",status);
        }
        if(StringUtils.isNotBlank(sidx)){
            wrapper.orderBy(sidx,order.equals("asc"));
        }*/
        //sql格式化过滤
        wrapper.addFilterIfNeed(params.get(CommonEnum.SQL_FILTER)!=null,(String)params.get(CommonEnum.SQL_FILTER)).orderBy("create_date",false);
        //执行查询并将查询结果添加到page
        page.setRecords(baseMapper.queryAllContract(page,wrapper));
        return new PageUtils(page);
    }

    //获取当前合同信息
    @Override
    public Contract getById(Long contractId) {
        Contract contract = baseMapper.contractById(contractId);
        return contract;
    }
}
