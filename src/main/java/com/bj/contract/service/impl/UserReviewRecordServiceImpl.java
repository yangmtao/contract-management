package com.bj.contract.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
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
 * @author yangmingtao
 * @since 2020-06-17
 */
@Service
public class UserReviewRecordServiceImpl extends ServiceImpl<UserReviewRecordMapper, UserReviewRecord> implements UserReviewRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception{
        EntityWrapper<UserReviewRecord> userReviewRecordWrapper = new EntityWrapper<>();
//        String supplierName =
//                null != params.get("supplierName") && StringUtils.isNotBlank(params.get("supplierName") + "") ? params.get("supplierName") + "" : "";
//        String creditCode = null != params.get("creditCode")
//                && StringUtils.isNotBlank(params.get("creditCode") + "") ? params.get("creditCode") + "" : "";
//        String blackList = null != params.get("blackList")
//                && StringUtils.isNotBlank(params.get("blackList") + "") ? params.get("blackList") + "" : "";
//        System.out.println("++++++++++++++++"+blackList);
//        if (StringUtils.isNotBlank(supplierName)) {
//            supplierWrapper.gt("instr(supplier_name,'" + supplierName + "')", 0);
//        }
//        if (StringUtils.isNotBlank(creditCode)) {
//            supplierWrapper.gt("instr(credit_code,'" + creditCode + "')", 0);
//        }
//        if (StringUtils.isNotBlank(blackList)) {
//            supplierWrapper.eq("black_list",Integer.parseInt(blackList));
//        }
        //supplierWrapper.orderBy("supplier_name", false);
        Page<UserReviewRecord> page = this.selectPage(
                new Query<UserReviewRecord>(params).getPage(),
                userReviewRecordWrapper
        );
       // page.setRecords(UserReviewRecordMapper.queryAllUser(page,wrapper));

        return new PageUtils(page);
    }
}
