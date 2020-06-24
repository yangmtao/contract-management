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

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-23
 */
@Service
public class ContractExamineServiceImpl extends ServiceImpl<ContractExamineMapper, ContractExamine> implements ContractExamineService {

    @Override
    public int save(ContractExamine examine) {
        return baseMapper.insert(examine);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception {
        Page page=new Query<ContractExamine>(params).getPage();
        Wrapper wrapper= SqlHelper.fillWrapper(page,new EntityWrapper<ContractExamine>());
        ContractExamine  contract= JSON.parseObject((String)params.get("contract"),ContractExamine.class);

        if (contract!=null){
            Field[] contractFields = contract.getClass().getDeclaredFields();

            for(int i=0;i<contractFields.length;i++){
                //获取属性名
                String propertie=contractFields[i].getName();
                //排除serialVersionUID
                if(!propertie.equals("serialVersionUID")){
                    //获取对象中每个属性对应的数据库字段名
                    String columName=contractFields[i].getAnnotation(TableField.class)!=null ? contractFields[i].getAnnotation(TableField.class).value() : "";
                    //获取该属性在当前对象中的值
                    contractFields[i].setAccessible(true);
                    Object value=contractFields[i].get(contract);
                    //构造SQL
                    //如果没有值，说明不需要构造该查询条件
                    if(value!=null){
                        if(propertie.equals("contractName")){
                            wrapper.gt("instr(contract_name,'"+contract.getContractName()+"')",0);
                        } else{
                            //未知字段名时先判空
                            if(StringUtils.isNotBlank(columName)){
                                wrapper.eq(columName,value);
                            }
                        }
                    }
                }

            }

        }
        wrapper.eq("1",1);

        //sql格式化过滤
        wrapper.addFilterIfNeed(params.get(CommonEnum.SQL_FILTER)!=null,(String)params.get(CommonEnum.SQL_FILTER)).orderBy("create_date",false);
        //执行查询并将查询结果添加到page
        page.setRecords(baseMapper.queryAllContractExamine(page,wrapper));
        return new PageUtils(page);
    }


}
