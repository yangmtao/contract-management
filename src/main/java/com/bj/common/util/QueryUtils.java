package com.bj.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 * @Author: lmf
 * @Date: 2019/3/28 15:28
 */
public class QueryUtils extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    /**当前页码*/
    private int page;
    /**每页条数*/
    private int limit;

    public QueryUtils(Map<String, Object> params){
        this.putAll(params);
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("offset", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
