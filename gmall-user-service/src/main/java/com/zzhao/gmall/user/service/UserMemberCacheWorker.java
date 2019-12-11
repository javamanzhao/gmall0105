package com.zzhao.gmall.user.service;

import com.alibaba.fastjson.JSON;
import com.zzhao.gmall.bean.UmsMember;
import com.zzhao.gmall.user.mapper.UserInfoMapper;
import com.zzhao.gmall.util.CacheWorker;
import com.zzhao.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/11/19 0019上午 9:58
 */
@Service
public class UserMemberCacheWorker extends CacheWorker<UmsMember, UmsMember> {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    protected UmsMember read(UmsMember umsMember) {
        UmsMember result = null;
        String key = "user:" + umsMember.getUsername() + ":" + umsMember.getPassword();
        Jedis jedis = redisUtil.getJedis();
        if (jedis.exists(key)) {
            String UmsMemberJson = jedis.get(key);
            if (StringUtils.isNotBlank(UmsMemberJson)) {
                result = JSON.parseObject(UmsMemberJson, UmsMember.class);
            }
        }
        jedis.close();
        return result;
    }

    @Override
    protected UmsMember write(UmsMember umsMember) {
        UmsMember result = null;
        Jedis jedis = redisUtil.getJedis();
        String key = "user:" + umsMember.getUsername() + ":" + umsMember.getPassword();
        result = userInfoMapper.selectOne(umsMember);
        if (result != null) {
            jedis.setex(key, 60 * 60 * 24, JSON.toJSONString(result));
        }
        jedis.close();
        return result;
    }


}
