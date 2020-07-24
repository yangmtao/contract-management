package com.bj.contract.service;

import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.contract.entity.UserReviewRecord;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-17
 */
public interface UserReviewRecordService extends IService<UserReviewRecord> {

    PageUtils queryPage(Map<String, Object> params,String userName) throws Exception;

    R saveUserReviewRecord(UserReviewRecord userReviewRecord);
}
