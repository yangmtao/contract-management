package com.bj.contract.controller;


import com.bj.common.enums.CommonEnum;
import com.bj.common.util.ExceptionUtil;
import com.bj.common.util.PageUtils;
import com.bj.common.util.R;
import com.bj.contract.entity.UserReviewRecord;
import com.bj.contract.service.UserReviewRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.bj.sys.controller.AbstractController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-17
 */
@RestController
@RequestMapping("/contract/userReviewRecord")
public class UserReviewRecordController extends AbstractController {
    @Autowired
    private UserReviewRecordService userReviewRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("contract:userReviewRecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = null;
        try {
            page = userReviewRecordService.queryPage(params);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("分页查询供应商信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "分页查询供应商信息出错");
        }

        return R.ok().put("page", page);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("contract:userReviewRecord:save")
    public R save(@Validated @RequestBody UserReviewRecord userReviewRecord){
        try {
            userReviewRecord.setReviewer(getUser().getUserName());
            System.out.println(userReviewRecord);
            userReviewRecord.setReviewResult("通过");
            return userReviewRecordService.saveUserReviewRecord(userReviewRecord);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("新增信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "修改信息出错");
        }

    }
/**
 * 驳回
 */
    @PostMapping("/reject")
    //@RequiresPermissions("contract:userReviewRecord:save")
    public R reject(@Validated @RequestBody UserReviewRecord userReviewRecord){
        try {
            userReviewRecord.setReviewer(getUser().getUserName());
            System.out.println(userReviewRecord);
            userReviewRecord.setReviewResult("驳回");
            return userReviewRecordService.saveUserReviewRecord(userReviewRecord);
        } catch (Exception e) {
            String msg = ExceptionUtil.getExceptionAllInformation(e);
            logger.error("新增信息出错，{}", msg);
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "修改信息出错");
        }

    }

}

