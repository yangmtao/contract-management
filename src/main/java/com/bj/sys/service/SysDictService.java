
package com.bj.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.bj.common.util.PageUtils;
import com.bj.sys.entity.SysDictEntity;

import java.util.Map;

/**
 * 数据字典
 *
 * @author zhph
 * @date 2020-04-21 14:26:20
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params) throws Exception;
}

