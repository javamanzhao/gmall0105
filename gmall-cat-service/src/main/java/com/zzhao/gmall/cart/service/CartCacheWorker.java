package com.zzhao.gmall.cart.service;

import com.alibaba.fastjson.JSON;
import com.zzhao.gmall.bean.OmsCartItem;
import com.zzhao.gmall.cart.mapper.OmsCartItemMapper;
import com.zzhao.gmall.util.CacheWorker;
import com.zzhao.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @author Administrator
 * @date 2019/11/15 0015下午 14:48
 */
@Service
public class CartCacheWorker extends CacheWorker<List<OmsCartItem>, String> {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Override
    protected List<OmsCartItem> read(String memberId) {
        Jedis jedis = redisUtil.getJedis();
        List<OmsCartItem> result = null;
        if (jedis.exists("user:" + memberId + ":cart")) {
            String vals = jedis.get("user:" + memberId + ":cart");
            result = JSON.parseArray(vals, OmsCartItem.class);
        }
        jedis.close();
        return result;
    }

    @Override
    protected List<OmsCartItem> write(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);
        // 同步到redis缓存中
        Jedis jedis = redisUtil.getJedis();
        if (omsCartItems != null && omsCartItems.size() > 0) {
            jedis.set("user:" + memberId + ":cart", JSON.toJSONString(omsCartItems));
        }
        jedis.close();
        return omsCartItems;

    }

}