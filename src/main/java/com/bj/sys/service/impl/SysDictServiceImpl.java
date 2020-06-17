
package com.bj.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
import com.bj.common.xss.SQLFilter;
import com.bj.sys.dao.SysDictDao;
import com.bj.sys.entity.SysDictEntity;
import com.bj.sys.service.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception {
        EntityWrapper<SysDictEntity> entityWrapper= new EntityWrapper<SysDictEntity>();

        String name = (String)params.get("name");
        if(StringUtils.isNotBlank(name)){
            entityWrapper.gt("instr(name,'"+name+"')",0);
        }
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = SQLFilter.sqlInject((String)params.get("sidx"));
        String order = SQLFilter.sqlInject((String)params.get("order"));
        if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(order)){
            entityWrapper.orderBy(sidx,"ASC".equalsIgnoreCase(order));
        }else{
            entityWrapper.orderBy("type,cast(code as signed)",true);
        }
        Page<SysDictEntity> page = this.selectPage(
                new Query<SysDictEntity>(params).getPage(),
                entityWrapper
        );

        return new PageUtils(page);
    }

}
