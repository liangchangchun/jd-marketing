package com.stylefeng.guns.core.redis.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import com.stylefeng.guns.core.redis.RedisService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisServiceImpl implements RedisService {
    private Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    private RedisTemplate redisTemplate;

    public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}
    
	public void init() {
		
	} 

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
     * set value
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            logger.error("set error: key {}, value {}",key,value,e);
        }
        return result;
    }
    public boolean setString(final String key, Object value) {
    	return set(key ,value);
    }

    /**
     * set value with expireTime
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            logger.error("set error: key {}, value {},expireTime {}",key,value,expireTime,e);
        }
        return result;
    }
    
    public boolean setString(final String key, Object value, Long expireTime) {
    	return set(key ,value ,expireTime);
    }
    
    public boolean setString(final String key, Object value, Integer expireTime) {
    	return set(key ,value ,expireTime * 1L);
    }

    /**
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    public boolean existsKey(final String key) {
        return exists(key);
    }

    /**
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }
    public String getString(final String key) {
        return String.valueOf(get(key));
    }

    /**
     * remove single key
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    public void delKey(final String key) {
    	remove(key);
    }

    /**
     * batch delete
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
    public void delKey(final String... keys) {
    	remove(keys);
    }

    /**
     * batch delete with pattern
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * hash set
     * @param key
     * @param hashKey
     * @param value
     */
    public void hashSet(String key, Object hashKey, Object value){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }
    public void addHashSet(String key, Object hashKey, Object value) {
    	hashSet( key,  hashKey,  value);
    }

    /**
     * hash get
     * @param key
     * @param hashKey
     * @return
     */
    public Object hashGet(String key, Object hashKey){
        HashOperations<String, Object, Object>  hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }
    
    public Map<Object,Object> hashGet(String key){
        HashOperations<String, Object, Object>  hash = redisTemplate.opsForHash();
        return hash.entries(key);
    }
    
    public String getHashSet(String key, Object hashKey){
        return String.valueOf(hashGet(key,hashKey));
    }

    /**
     *  list push
     * @param k
     * @param v
     */
    public void push(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }

    /**
     *  list range
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> range(String k, long l, long l1){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,l,l1);
    }

    /**
     *  set add
     * @param key
     * @param value
     */
    public void setAdd(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }

    /**
     * set get
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * ordered set add
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key,Object value,double scoure){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,scoure);
    }

    /**
     * rangeByScore
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key,double scoure,double scoure1){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

	@Override
	public long getSCard(String key) {
		 ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		return zset.zCard(key);
	}
}