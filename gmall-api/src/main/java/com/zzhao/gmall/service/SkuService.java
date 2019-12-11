package com.zzhao.gmall.service;

import com.zzhao.gmall.bean.PmsSkuImage;
import com.zzhao.gmall.bean.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/11/5 0005下午 17:34
 */
public interface SkuService {
    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId);


    List<PmsSkuInfo> getAllSku(String catalog3Id);

    boolean checkPrice(String productSkuId, BigDecimal price);

    PmsSkuInfo getPmsSkuInfo(String skuId);

    List<PmsSkuImage> getPmsSkuImages(String skuId);
}
