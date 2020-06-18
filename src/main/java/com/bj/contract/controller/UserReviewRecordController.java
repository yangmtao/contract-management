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
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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


}

