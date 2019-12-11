package com.zzhao.gmall.util;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collections;


/**
 * @author Administrator
 * @date 2019/11/8 0008下午 16:13
 */
@Component
public class RedisLock {

    private String lock_key = "redis_lock"; //锁键

    private long timeout = 999999; //获取锁的超时时间


    @Autowired
    RedisUtil redisUtil;

    public boolean lock(String lockKey, String uuid) {
        Jedis jedis = redisUtil.getJedis();
        Long start = System.currentTimeMillis();
        try {
            for (; ; ) {
                //SET命令返回OK ，则证明获取锁成功
                String lock = jedis.set(lockKey, uuid, "nx", "px", 5);
                if ("OK".equals(lock)) {
                    return true;
                }
                //否则循环等待，在timeout时间内仍未获取到锁，则获取失败
                long l = System.currentTimeMillis() - start;
                if (l >= timeout) {
                    return false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            jedis.close();
        }
    }

    public boolean unlock(String lockKey, String uuid) {
        Jedis jedis = redisUtil.getJedis();
        String script =
                "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                        "   return redis.call('del',KEYS[1]) " +
                        "else" +
                        "   return 0 " +
                        "end";
        try {
            Object result = jedis.eval(script, Collections.singletonList(lockKey),
                    Collections.singletonList(uuid));
            if ("1".equals(result.toString())) {
                return true;
            }
            return false;
        } finally {
            jedis.close();
        }
    }

}
