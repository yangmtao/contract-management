package com.bj.contract.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
import com.bj.common.util.R;
import com.bj.contract.entity.Contract;
import com.bj.contract.entity.ContractSettlement;
import com.bj.contract.entity.UserReviewRecord;
import com.bj.contract.dao.UserReviewRecordMapper;
import com.bj.contract.service.UserReviewRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
public class UserReviewRecordServiceImpl extends ServiceImpl<UserReviewRecordMapper, UserReviewRecord> implements UserReviewRecordService {


    /**
     * 用户审查记录查询
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception{

        EntityWrapper<ContractSettlement> reviewWrapper = new EntityWrapper<>();
        String contractName = null != params.get("contractName")
                && StringUtils.isNotBlank(params.get("contractName") + "") ? params.get("contractName") + "" : "";
        String reviewer = null != params.get("reviewer")
                && StringUtils.isNotBlank(params.get("reviewer") + "") ? params.get("reviewer") + "" : "";

        if (StringUtils.isNotBlank(contractName)) {
            reviewWrapper.gt("instr(contract_name,'" + contractName + "')", 0);
        }
        if (StringUtils.isNotBlank(reviewer)) {
            reviewWrapper.gt("instr(reviewer,'" + reviewer + "')", 0);
        }
        Page<Contract> page = new Query<Contract>(params).getPage();
        page.setRecords(baseMapper.queryReview(page,reviewWrapper));


        return new PageUtils(page);
    }


    //审核记录表
    @Override
    public R saveUserReviewRecord(UserReviewRecord userReviewRecord) {

        if(userReviewRecord.getReviewResult().equals("通过")){
            baseMapper.updateNode(userReviewRecord.getContractId(),1);
        }else {
            baseMapper.updateNode(userReviewRecord.getContractId(),-1);
        }
        baseMapper.insert(userReviewRecord);
        return R.ok();
    }
}
