package com.stylefeng.guns.core.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {

    /**
     * set value
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) ;
    public boolean setString(final String key, Object value) ;
    /**
     * set value with expireTime
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) ;
    public boolean setString(final String key, Object value, Long expireTime) ;
    public boolean setString(final String key, Object value, Integer expireTime) ;
    /**
     * @param key
     * @return
     */
    public boolean exists(final String key);
    public boolean existsKey(final String key);

    /**
     * @param key
     * @return
     */
    public Object get(final String key) ;
    public String getString(final String key) ;

    /**
     * remove single key
     * @param key
     */
    public void remove(final String key);
    public void delKey(final String key);
    /**
     * batch delete
     * @param keys
     */
    public void remove(final String... keys) ;
    public void delKey(final String... keys);
    /**
     * batch delete with pattern
     * @param pattern
     */
    public void removePattern(final String pattern) ;

    /**
     * hash set
     * @param key
     * @param hashKey
     * @param value
     */
    public void hashSet(String key, Object hashKey, Object value);
    
    public void addHashSet(String key, Object hashKey, Object value);

    /**
     * hash get
     * @param key
     * @param hashKeys
     * @return
     */
    public Object hashGet(String key, Object hashKey);
    public String getHashSet(String key, Object hashKey);
    public Map hashGet(String key);
    /**
     *  list push
     * @param k
     * @param v
     */
    public void push(String k,Object v);

    /**
     *  list range
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> range(String k, long l, long l1);

    /**
     *  set add
     * @param key
     * @param value
     */
    public void setAdd(String key,Object value);

    /**
     * set get
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key);

    /**
     * ordered set add
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key,Object value,double scoure);

    /**
     * rangeByScore
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key,double scoure,double scoure1);
    
    // 获取集合成员数
    public long getSCard(String key);
}
