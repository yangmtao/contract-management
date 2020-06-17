package com.bj.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
import com.bj.common.xss.SQLFilter;
import com.bj.sys.dao.BjUserOperationDao;
import com.bj.sys.entity.UserOperationEntity;
import com.bj.sys.form.LogForm;
import com.bj.sys.service.BjUserOperationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Service
public class BjUserOperationServiceImpl extends ServiceImpl<BjUserOperationDao, UserOperationEntity> implements BjUserOperationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception {
        Page<UserOperationEntity> page = this.selectPage(
                new Query<UserOperationEntity>(params).getPage(),
                new EntityWrapper<UserOperationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageBySql(Map<String, Object> params) throws Exception {
        String sidx = SQLFilter.sqlInject((String) params.get("sidx"));
        String order = SQLFilter.sqlInject((String) params.get("order"));
        String page = params.get("page") != null ? params.get("page") + "" : "1";
        String limit = params.get("limit") != null ? params.get("limit") + "" : "10";
        String orderByStr = getOrderStr(sidx, order);
        params.put("sidx", orderByStr);
        int startNum = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
        params.put("startNum", startNum);
        List<UserOperationEntity> lists = baseMapper.queryUserOperationPage(params);
        int total = baseMapper.queryUserOperationTotal(params);
        PageUtils pageUtil = new PageUtils(lists, total, Integer.parseInt(limit), Integer.parseInt(page));
        return pageUtil;
    }

    @Override
    public PageUtils queryPageBySqlFront(Map<String, Object> params) throws Exception {
        String sidx = SQLFilter.sqlInject((String) params.get("sidx"));
        String order = SQLFilter.sqlInject((String) params.get("order"));
        String page = params.get("page") != null ? params.get("page") + "" : "1";
        String limit = params.get("limit") != null ? params.get("limit") + "" : "10";
        String orderByStr = getOrderStr(sidx, order);

        params.put("sidx", orderByStr);
        int startNum = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
        params.put("startNum", startNum);
        List<UserOperationEntity> lists = baseMapper.queryUserOperationPageFront(params);
        int total = baseMapper.queryUserOperationTotalFront(params);
        PageUtils pageUtil = new PageUtils(lists, total, Integer.parseInt(limit), Integer.parseInt(page));
        return pageUtil;
    }

    @Override
    public PageUtils queryPageFromMongoDb(Map<String, Object> params) throws Exception {
        String page = params.get("page") != null ? params.get("page") + "" : "1";
        String limit = params.get("limit") != null ? params.get("limit") + "" : "10";
        String opType = params.get("opType") != null ? params.get("opType") + "" : "";
        String userName = params.get("userName") != null && StringUtils.isNotBlank(params.get("userName") + "") ? params.get("userName") + "" : "";
        String entName = params.get("entName") != null && StringUtils.isNotBlank(params.get("entName") + "") ? params.get("entName") + "" : "";
        String entId = params.get("entId") != null && StringUtils.isNotBlank(params.get("entId") + "") ? params.get("entId") + "" : "";
        String createDateStart = params.get("createDateStart") != null && StringUtils.isNotBlank(params.get("createDateStart") + "") ? params.get("createDateStart") + "" : "";
        String createDateEnd = params.get("createDateEnd") != null && StringUtils.isNotBlank(params.get("createDateEnd") + "") ? params.get("createDateEnd") + "" : "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(opType)) {
            criteria.and("opType").is(Integer.valueOf(opType));
        }
        if (StringUtils.isNotBlank(userName)) {
            Pattern pattern = Pattern.compile("^" + userName + ".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("userName").regex(pattern);
        }
        if (StringUtils.isNotBlank(entName)) {
            Pattern pattern = Pattern.compile("^.*" + entName + ".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("entName").regex(pattern);
        }
        if (StringUtils.isNotBlank(entId)) {
            Pattern pattern = Pattern.compile("^.*" + entId + ".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("entId").is(pattern);
        }
        if (StringUtils.isNotBlank(createDateStart) && StringUtils.isBlank(createDateEnd)) {
            criteria.and("createDate").gte(simpleDateFormat.parse(createDateStart));
        }
        if (StringUtils.isBlank(createDateStart) && StringUtils.isNotBlank(createDateEnd)) {
            criteria.and("createDate").lte(simpleDateFormat.parse(createDateEnd));
        }

        if (StringUtils.isNotBlank(createDateStart) && StringUtils.isNotBlank(createDateEnd)) {
            criteria.and("createDate").gte(simpleDateFormat.parse(createDateStart)).lte(simpleDateFormat.parse(createDateEnd));
        }

        query.addCriteria(criteria);
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit));
        long total = mongoTemplate.count(query, LogForm.class);
        List<LogForm> resolveRules = mongoTemplate.find(query.with(pageable).with(Sort.by(Sort.Order.desc("createDate"))), LogForm.class);

        return new PageUtils(resolveRules, Integer.valueOf(total + ""), Integer.parseInt(limit), Integer.parseInt(page));
    }

    /**
     * 生成排序条件
     *
     * @param sidx  排序字段
     * @param order 倒序or升序
     * @return
     */
    private String getOrderStr(String sidx, String order) {
        String orderByStr = "";
        if (StringUtils.isBlank(sidx)) {
            orderByStr = "op_date desc ";
        } else {
            if (StringUtils.isBlank(order)) {
                order = " desc";
            }
            orderByStr = sidx + " " + order;
        }
        return orderByStr;
    }
}
