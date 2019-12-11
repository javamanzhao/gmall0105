package com.zzhao.gmall.manage.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zzhao.gmall.bean.PmsBaseAttrInfo;
import com.zzhao.gmall.bean.PmsSkuAttrValue;
import com.zzhao.gmall.bean.PmsSkuImage;
import com.zzhao.gmall.bean.PmsSkuInfo;
import com.zzhao.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.zzhao.gmall.manage.mapper.PmsSkuImageMapper;
import com.zzhao.gmall.manage.mapper.PmsSkuInfoMapper;
import com.zzhao.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.zzhao.gmall.service.SkuService;
import com.zzhao.gmall.util.RedisLock;
import com.zzhao.gmall.util.RedisUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;


/**
 * @author Administrator
 * @date 2019/11/5 0005下午 17:34
 */
@Service(retries = -1)
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedissonClient redissonClient;

    @Override
    @Transactional(propagation = REQUIRED)
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        pmsSkuInfo.getSkuImageList().forEach(pmsSkuImage -> {
            pmsSkuImage.setSkuId(pmsSkuInfo.getId());
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        });
        pmsSkuInfo.getSkuAttrValueList().forEach(pmsSkuAttrValue -> {
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        });
        pmsSkuInfo.getSkuSaleAttrValueList().forEach(pmsSkuSaleAttrValue -> {
            pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        });
        return "success";
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        Jedis jedis = redisUtil.getJedis();
        PmsSkuInfo pmsSkuInfo = null;
        RLock lock = redissonClient.getLock("lock");
        //String uuid = UUID.randomUUID().toString();
        try {
            if (jedis.exists("sku" + skuId + "info")) {
                String skuIdStr = jedis.get("sku" + skuId + "info");
                JSONObject jsonObject = JSON.parseObject(skuIdStr);
                pmsSkuInfo = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<PmsSkuInfo>() {
                });
            } else {
                lock.lock(5, TimeUnit.SECONDS);
                pmsSkuInfo = getSkuByIdFromDb(skuId);
                jedis.set("sku" + skuId + "info", JSON.toJSONString(pmsSkuInfo));
                lock.unlock();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return pmsSkuInfo;
    }

    public PmsSkuInfo getSkuByIdFromDb(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        pmsSkuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        pmsSkuInfo.setSkuImageList(pmsSkuImageMapper.select(pmsSkuImage));
        return pmsSkuInfo;
    }

    public PmsSkuInfo getPmsSkuInfo(String skuId){
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        return pmsSkuInfoMapper.selectOne(pmsSkuInfo);
    }

    public List<PmsSkuImage> getPmsSkuImages(String skuId){
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        return pmsSkuImageMapper.select(pmsSkuImage);
    }
    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {
        return pmsSkuInfoMapper.getSkuSaleAttrValueListBySpu(productId);
    }

    @Override
    public List<PmsSkuInfo> getAllSku(String catalog3Id) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String skuId = pmsSkuInfo.getId();

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);

            pmsSkuInfo.setSkuAttrValueList(select);
        }
        return pmsSkuInfos;
    }

    @Override
    public boolean checkPrice(String productSkuId, BigDecimal productPrice) {
        boolean b = false;
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(productSkuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        BigDecimal price = pmsSkuInfo1.getPrice();

        if(price.compareTo(productPrice)==0){
            b = true;
        }
        return b;
    }
}
