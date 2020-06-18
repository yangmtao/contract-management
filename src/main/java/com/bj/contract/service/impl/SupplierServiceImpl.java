package com.bj.contract.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bj.common.enums.CommonEnum;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
import com.bj.common.util.R;
import com.bj.contract.dao.SupplierDao;
import com.bj.contract.entity.SupplierEntity;
import com.bj.contract.service.SupplierService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("supplierService")
public class SupplierServiceImpl extends ServiceImpl<SupplierDao, SupplierEntity> implements SupplierService {



    //分页查询数据
    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception {
        EntityWrapper<SupplierEntity> supplierWrapper = new EntityWrapper<>();
        String supplierName =
                null != params.get("supplierName") && StringUtils.isNotBlank(params.get("supplierName") + "") ? params.get("supplierName") + "" : "";
        String creditCode = null != params.get("creditCode")
                && StringUtils.isNotBlank(params.get("creditCode") + "") ? params.get("creditCode") + "" : "";
        String blackList = null != params.get("blackList")
                && StringUtils.isNotBlank(params.get("blackList") + "") ? params.get("blackList") + "" : "";
        System.out.println("++++++++++++++++"+blackList);
        if (StringUtils.isNotBlank(supplierName)) {
            supplierWrapper.gt("instr(supplier_name,'" + supplierName + "')", 0);
        }
        if (StringUtils.isNotBlank(creditCode)) {
            supplierWrapper.gt("instr(credit_code,'" + creditCode + "')", 0);
        }
        if (StringUtils.isNotBlank(blackList)) {
            supplierWrapper.eq("black_list",Integer.parseInt(blackList));
        }
        supplierWrapper.orderBy("supplier_name", false);
        Page<SupplierEntity> page = this.selectPage(
                new Query<SupplierEntity>(params).getPage(),
                supplierWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public boolean save(SupplierEntity supplierEntity) {
        int i = baseMapper.insert(supplierEntity);
        boolean a = false;
        if (i > 0) {
            a = true;
        }
        return a;
    }

    @Override
    public boolean updateById(SupplierEntity supplierEntity) {
        int i = baseMapper.updateById(supplierEntity);
        boolean a = false;
        if (i > 0) {
            a = true;
        }
        return a;
    }
//    @Override
//    public boolean deleteBatchIds(Long[] supplierIds){
//        int i = baseMapper.deleteBatchIds(Arrays.asList(supplierIds));
//        boolean a = false;
//        if (i > 0) {
//            a = true;
//        }
//        return a;
//    }

    @Override
    public SupplierEntity getById(Long supplierId) {
        SupplierEntity supplier = baseMapper.selectById(supplierId);
        return supplier;
    }

    /**
     * 更新供应商信息
     * @param supplier
     * @return
     */
    @Override
    public R updateSupplier(SupplierEntity supplier) {
        if (supplier.getSupplierId() == null) {
            return R.error(CommonEnum.ReturnCode.PARAM.getValue(), "缺少参数[userId]！");
        }
        SupplierEntity Supplier = baseMapper.selectById(supplier.getSupplierId());
        if (Supplier == null) {
            return R.error(CommonEnum.ReturnCode.PARAM.getValue(), "未获取到客户信息！");
        }
        Supplier.setSupplierName(supplier.getSupplierName());
        Supplier.setCreditCode(supplier.getCreditCode());
        Supplier.setLegaRepresentative(supplier.getLegaRepresentative());
        Supplier.setAttribution(supplier.getAttribution());
        Supplier.setRegisteredCapital(supplier.getRegisteredCapital());
        Supplier.setOperatingStatus(supplier.getOperatingStatus());
        Supplier.setSupplierType(supplier.getSupplierType());
        Supplier.setBusinessScope(supplier.getBusinessScope());
        Supplier.setStatus(supplier.getStatus());
        Supplier.setQualifications(supplier.getQualifications());
        Supplier.setRemarks(supplier.getRemarks());

        baseMapper.updateById(Supplier);
        return R.ok();
    }


    /**
     * 新增供应商
     * @param supplier
     * @return
     */
    @Override
    public R saveSupplier(SupplierEntity supplier) {
//        finUserEntity.setUserId(SnowflakeIdWorker.getInstance().nextId());
//        finUserEntity.setUserPwd(ShiroUtils.sha256(finUserEntity.getUserPwd(), Constant.pwdSalt));
//        finUserEntity.setUserStatus(CommonEnum.DataStatus.ENABLE.getValue());
//        if (finUserEntity.getUserFlag() == null) {
//            finUserEntity.setUserFlag(CommonEnum.UserFlag.MASTER.getValue());
//        }
//        finUserEntity.setCreateTime(new Date());
//        finUserEntity.setCreatePersonId(ShiroUtils.getUserId());
//        finUserEntity.setCreatePersonName(ShiroUtils.getUserEntity().getRealName());

        baseMapper.insert(supplier);

        return R.ok();
    }


    /**
     * 移交黑名单
     * @param supplierId
     * @param blackList
     * @param remarks
     * @return
     */
    @Override
    public R update(Long supplierId, Integer blackList, String remarks) {
        if (supplierId == null) {
            return R.error(CommonEnum.ReturnCode.PARAM.getValue(), "缺少参数[userId]！");
        }
        SupplierEntity Supplier = baseMapper.selectById(supplierId);
        if (Supplier == null) {
            return R.error(CommonEnum.ReturnCode.PARAM.getValue(), "未获取到客户信息！");
        }
        Supplier.setRemarks(remarks);
        Supplier.setBlackList(blackList);
//        Integer update = baseMapper.update(Supplier,
//                new EntityWrapper<SupplierEntity>()
//                .eq("supplier_id", supplierId)
//                .eq("black_list", blackList)
//                .eq("remarks", remarks));
        Integer updateById = baseMapper.updateById(Supplier);
        System.out.println(updateById);
        return R.ok();

    }

    @Override
    public List<Map<String, String>> selectSupplierSimple(String keyword) {
        return baseMapper.selectSupplierSimple(keyword);
    }

}