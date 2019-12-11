package com.zzhao.gmall.cart.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.zzhao.gmall.bean.OmsCartItem;
import com.zzhao.gmall.cart.mapper.OmsCartItemMapper;
import com.zzhao.gmall.cart.service.CartCacheWorker;
import com.zzhao.gmall.service.CartService;
import com.zzhao.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author Administrator
 * @date 2019/11/15 0015下午 14:13
 */
@Service(retries = -1)
public class CartServiceImpl implements CartService {

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Autowired
    CartCacheWorker cartCacheWorker;

    @Autowired
    RedisUtil redisUtil;


    @Override
    public OmsCartItem ifCartExistByUser(String memberId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItem1 = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem1;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        List<OmsCartItem> list = cartCacheWorker.find(omsCartItem.getMemberId());
        if (StringUtils.isNotBlank(omsCartItem.getMemberId())) {
            omsCartItemMapper.insertSelective(omsCartItem);//避免添加空值
        }
        if (CollectionUtils.isNotEmpty(list)) {
            list.add(omsCartItem);
        } else {
            list = list == null ? Collections.EMPTY_LIST : list;
            list.add(omsCartItem);
        }
        redisUtil.getJedis().set("user:" + omsCartItem.getMemberId() + ":cart", JSON.toJSONString(list));
        redisUtil.getJedis().close();
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {
        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("id", omsCartItemFromDb.getId());
        omsCartItemMapper.updateByExampleSelective(omsCartItemFromDb, e);
        redisUtil.getJedis().del("user:" + omsCartItemFromDb.getMemberId() + ":cart");
        redisUtil.getJedis().close();
    }

    @Override
    public List<OmsCartItem> cartList(String memberId) {
        return cartCacheWorker.find(memberId);
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {
        Example e = new Example(OmsCartItem.class);

        e.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductSkuId());

        omsCartItemMapper.updateByExampleSelective(omsCartItem,e);

        // 缓存同步
        redisUtil.getJedis().del("user:" + omsCartItem.getMemberId() + ":cart");
        redisUtil.getJedis().close();

    }

}
