package com.bj.cache;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bj.sys.entity.SysDictEntity;
import com.bj.sys.service.SysDictService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * 字典缓存管理
 *
 * @author zhp
 */
@Component
public class DictCache {

    private static Logger logger = LoggerFactory.getLogger(DictCache.class);
    @Resource
    private SysDictService dictService;

    @Resource
    private JedisPool jedisPool;

    public static DictCache instanceDict = null;


    /**
     * 缓存所有的字典信息
     */
    private static Map<String, List<Map<String, String>>> CACHE_MAP_DICT = new HashMap<>();


    @PostConstruct
    public void init() {
        if (instanceDict == null) {
            instanceDict = new DictCache();
        }
        instanceDict.jedisPool = jedisPool;
        instanceDict.dictService = dictService;
        initDictCacheMap();
//        System.out.println("字典缓存信息："+JSON.toJSONString(CACHE_MAP_DICT));

    }

    private void initDictCacheMap() {
        EntityWrapper<SysDictEntity> dictWrapper = new EntityWrapper<>();
        dictWrapper.eq("del_flag", 0);
        List<SysDictEntity> dictList = instanceDict.dictService.selectList(dictWrapper);

        if (null != dictList && !dictList.isEmpty()) {
            Map<String, String> map1 = new HashMap<>(dictList.size());
            for (SysDictEntity dict : dictList) {
                map1.put(dict.getType(), dict.getName());
            }
            if (null != map1 && !map1.isEmpty()) {
                Iterator<Map.Entry<String, String>> it = map1.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    String key = entry.getKey();
                    List<Map<String, String>> lists = this.buildMap(dictList, key);
                    if (null != lists && lists.size() > 0) {
                        CACHE_MAP_DICT.put(key, lists);
                    }
                }
            }
            initRedisMap(CACHE_MAP_DICT);
        }

    }

    /**
     * 组装字典信息的map
     *
     * @param dictList
     * @param dictKey
     * @return
     */
    private List<Map<String, String>> buildMap(List<SysDictEntity> dictList, String dictKey) {
        List<Map<String, String>> lists = new ArrayList<>();
        if (null != dictList && !dictList.isEmpty()) {
            for (SysDictEntity dict : dictList) {
                String type = dict.getType();
                String name = dict.getName();
                String code = dict.getCode();
                String value = dict.getValue();
                if (type.equals(dictKey)) {
                    Map<String, String> temp = new HashMap<>();
                    temp.put("name", name);
                    temp.put("type", type);
                    temp.put("code", code);
                    temp.put("value", value);
                    lists.add(temp);
                }
            }
        }
        return lists;
    }

    public void initRedisMap(Map<String, List<Map<String, String>>> map) {
        Jedis jedis = null;
        try {
            logger.info("===initRedisMap==获取redis的连接==={}", System.currentTimeMillis());
            jedis = instanceDict.jedisPool.getResource();
            jedis.select(1);
            if (null != map && !map.isEmpty()) {
                //移除redis中缓存的字典信息，重新储存字典信息
                String keyName = "sys:config";
                this.removeDictInfo(jedis, keyName);
                Iterator<Map.Entry<String, List<Map<String, String>>>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, List<Map<String, String>>> entry = it.next();
                    String key = entry.getKey();
                    List<Map<String, String>> lists = map.get(key);

                    this.addRedisItem(jedis, keyName, key, lists);
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
                logger.info("===initRedisMap==执行关闭redis的操作==={}", System.currentTimeMillis());
            }
        }
    }

    /**
     * 移除jedis 的key
     *
     * @param redis
     * @param key
     */
    private void removeDictInfo(Jedis redis, String key) {
        Set<String> set = redis.hkeys(key);
        if (null != set && set.size() > 0) {
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                redis.hdel(key, iterator.next());
            }
        }
    }

    /**
     * 往字典缓存map和redis中添加新的字典信息
     *
     * @param type
     * @param dictEntity
     */
    public void addDict(String type, SysDictEntity dictEntity, SysDictEntity oldEntity) {
        Jedis jedis = null;
        try {
            logger.info("===addDict==获取redis的连接==={}", System.currentTimeMillis());
            jedis = instanceDict.jedisPool.getResource();
            jedis.select(1);
            List<Map<String, String>> dictList = this.checkDict(type, oldEntity);
            Map<String, String> temp = new HashMap<>();
            temp.put("name", dictEntity.getName());
            temp.put("type", dictEntity.getType());
            temp.put("value", dictEntity.getValue());
            temp.put("code", dictEntity.getCode());

            if (!(null != dictList && !dictList.isEmpty())) {
                dictList = new ArrayList<>();
            }
            dictList.add(temp);
            CACHE_MAP_DICT.put(type, dictList);
            String key = "sys:config";
            this.addRedisItem(jedis, key, type, dictList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
                logger.info("===addDict==执行关闭redis的操作==={}", System.currentTimeMillis());
            }
        }
    }


    /**
     * 从缓存map和redis中移除该字典信息
     *
     * @param type
     * @param dictEntity
     */
    public void delDict(String type, SysDictEntity dictEntity) {
        Jedis jedis = null;
        try {
            logger.info("===delDict==获取redis的连接==={}", System.currentTimeMillis());
            jedis = instanceDict.jedisPool.getResource();
            jedis.select(1);
            String key = "sys:config";
            List<Map<String, String>> dictList = this.checkDict(type, dictEntity);
            if (null != dictList && !dictList.isEmpty()) {
                CACHE_MAP_DICT.put(type, dictList);
                this.addRedisItem(jedis, key, type, dictList);
            } else {
                CACHE_MAP_DICT.remove(type);
                jedis.hdel(key, type);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
                logger.info("===delDict==执行关闭redis的操作==={}", System.currentTimeMillis());
            }
        }
    }

    /**
     * 根据key值获取字典信息
     *
     * @param type
     * @return
     */
    public List<Map<String, String>> getDictListByKey(String type) {
        return CACHE_MAP_DICT.get(type);
    }

    /**
     * 去掉缓存中已有的字典信息
     *
     * @param type
     * @param dictEntity
     * @return
     */
    private List<Map<String, String>> checkDict(String type, SysDictEntity dictEntity) {
        List<Map<String, String>> result = new ArrayList<>();
        List<Map<String, String>> dictList = CACHE_MAP_DICT.get(type);
        if (CollectionUtils.isNotEmpty(dictList)) {
            for (int i = 0; i < dictList.size(); i++) {
                Map<String, String> map = dictList.get(i);
                String code = map.get("code");
                if (dictEntity != null) {
                    if (!code.equals(dictEntity.getCode())) {
                        result.add(map);
                    }
                } else {
                    result.add(map);
                }
            }
        }
        return result;
    }

    /**
     * 往redis中添加配置信息
     *
     * @param type
     * @param lists
     */
    private void addRedisItem(Jedis redis, String key, String type, List<Map<String, String>> lists) {
        JSONArray dictData = new JSONArray();
        if (CollectionUtils.isNotEmpty(lists)) {
            for (Map<String, String> map : lists) {
                JSONObject jsonObject = new JSONObject();
                Iterator<Map.Entry<String, String>> dictIt = map.entrySet().iterator();
                while (dictIt.hasNext()) {
                    Map.Entry<String, String> dictEntry = dictIt.next();
                    jsonObject.put(dictEntry.getKey(), map.get(dictEntry.getKey()));
                }
                dictData.add(jsonObject);
            }
        }
        redis.hset(key, type, dictData.toJSONString());
    }
}
