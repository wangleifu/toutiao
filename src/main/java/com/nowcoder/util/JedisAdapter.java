package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * redis 操作、服务层
 *
 * @author wangleifu
 * @create 2018-12-20 11:25
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);
    }

    public String get(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        }
    }

    public void set(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
    }

    public long sadd(String key, String value) {
        try (Jedis jedis = pool.getResource()){
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("redis操作异常 " + e.getMessage());
            return 0;
        }
    }

    public long srem(String key, String value) {
        try(Jedis jedis = pool.getResource()) {
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("redis操作异常 " + e.getMessage());
            return 0;
        }
    }

    public long scard(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("redis操作异常 " + e.getMessage());
            return 0;
        }
    }

    public boolean sismember(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("redis操作异常 " + e.getMessage());
            return false;
        }
    }

    public void setex(String key, String value) {
        // 验证码, 防机器注册，记录上次注册时间，有效期3天
        try (Jedis jedis = pool.getResource()) {
            jedis.setex(key, 10, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
    }

    public long lpush(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        }
    }

    public List<String> brpop(int timeout, String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        }
    }

    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }
}
