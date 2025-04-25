package com.yun.lottery.common.utils;

/**
 * @author yun
 * @date 2025/4/14 10:04
 * @desciption:
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class RedisUtil {
    /**
     * StringRedisTemplate继承RedisTemplate<String, String>，两种的区别仅仅是序列化
     * ⽅式不⼀样。
     * 这⾥选⽤StringRedisTemplate，能够避免乱码问题。 直接存放的就是string 可读的
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("error occurred in RedisUtil.hasKey({}) ---> ", key,
                    e);
            return false;
        }
    }

    /**
     * 指定key的过期时间为time，单位是秒
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean setExpire(String key, long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("error occurred in RedisUtil.setExpire({}, {}) ---> ", key, time, e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永不过期
     */
    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * y
     *
     * @param key 可以传⼀个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                stringRedisTemplate.delete(key[0]);
            } else {
                stringRedisTemplate.delete((Collection<String>)
                        CollectionUtils.arrayToList(key));
            }
        }
    }
//============================String类型的⽅法=============================
//存储格式为： key = value

    /**
     * 获取指定键的值
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        log.info("redis get key:{}", key);
        return key == null ? null : stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 设置指定键的值，如果存在就是更新操作
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("error occurred in RedisUtil.set({},{}) ---> ", key,
                    value, e);
            return false;
        }
    }

    /**
     * 设置指定键的值，同时设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要⼤于0 如果time⼩于等于0 将设置⽆限期
     * @return true成功 false 失败
     */
    public boolean set(String key, String value, long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.opsForValue().set(key, value, time,
                        TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            log.info("redis set key:{}", key);
            return true;
        } catch (Exception e) {
            log.error("error occurred in RedisUtil.set({},{},{}) ---> ",
                    key, value, time, e);
            return false;
        }
    }
}
