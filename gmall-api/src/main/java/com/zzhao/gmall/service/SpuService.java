package com.zzhao.gmall.service;

import com.zzhao.gmall.bean.PmsBaseSaleAttr;
import com.zzhao.gmall.bean.PmsProductImage;
import com.zzhao.gmall.bean.PmsProductInfo;
import com.zzhao.gmall.bean.PmsProductSaleAttr;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/11/4 0004下午 14:46
 */
public interface SpuService {

    List<PmsProductInfo> spuList(String catalog3Id);

    String saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
