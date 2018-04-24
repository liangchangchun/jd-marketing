package com.ttt.test;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class HttpRequestTest {

	@LocalServerPort
    private int port;

	 @Autowired
	 RedisTemplate redisTemplate;
    
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
   }
    
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            System.out.println("set error: key "+key+", value "+value+",expireTime "+expireTime);
        }
        return result;
    }
    
    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {

    }
}
