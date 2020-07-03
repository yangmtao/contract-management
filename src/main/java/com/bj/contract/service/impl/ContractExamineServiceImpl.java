package com.bj.contract.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bj.common.enums.CommonEnum;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
import com.bj.contract.entity.Contract;
import com.bj.contract.entity.ContractExamine;
import com.bj.contract.dao.ContractExamineMapper;
import com.bj.contract.service.ContractExamineService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  合同审查 服务实现类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-23
 */
@Service
public class ContractExamineServiceImpl extends ServiceImpl<ContractExamineMapper, ContractExamine> implements ContractExamineService {

    //保存合同审查信息
    @Override
    public int save(ContractExamine examine) {
        return baseMapper.insert(examine);
    }

    //根据查询条件分页查询所有的相关合同审查信息
    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception {
        Page page=new Query<ContractExamine>(params).getPage();
        Wrapper wrapper= SqlHelper.fillWrapper(page,new EntityWrapper<ContractExamine>());

        Object[] keys=params.keySet().toArray();
        //拼接SQL
        for(int i=0;i<keys.length;i++){

            String key=(String)keys[i];
            //只有符合该正则表达式的才为查询条件
            if(key.matches("[a-z]+_[a-z]+")){
                String value=params.get(key)!=null||params.get(key)!="" ? (String)params.get(key):"";
                //如果该查询参数有值，说明需要拼接为查询条件
                if(!value.equals("")){
                    //合同名称使用instr函数查询
                    if(key.equals("contract_name")){
                        wrapper.gt("instr(contract_name,'"+value+"')",0);
                    }else{
                        wrapper.eq((String)keys[i],value);
                    }

                }
            };

        }

        wrapper.eq("1",1);

        //sql格式化过滤
        wrapper.addFilterIfNeed(params.get(CommonEnum.SQL_FILTER)!=null,(String)params.get(CommonEnum.SQL_FILTER)).orderBy("create_date",false);
        //执行查询并将查询结果添加到page
        page.setRecords(baseMapper.queryAllContractExamine(page,wrapper));
        return new PageUtils(page);
    }

    //根据审查记录id获取对应的合同审查信息
    @Override
    public ContractExamine selectInfoById(Integer id) {
        return baseMapper.selectInfoById(id);
    }


}
