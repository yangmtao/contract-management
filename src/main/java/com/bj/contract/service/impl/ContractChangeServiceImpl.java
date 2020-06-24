package com.bj.contract.service.impl;

import com.bj.contract.entity.ContractChange;
import com.bj.contract.dao.ContractChangeMapper;
import com.bj.contract.service.ContractChangeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-17
 */
@Service
public class ContractChangeServiceImpl extends ServiceImpl<ContractChangeMapper, ContractChange> implements ContractChangeService {

    @Override
    public int save(ContractChange change) {
        change.setCreateDate(new Date());
        return baseMapper.insert(change);
    }
}
