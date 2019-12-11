package com.zzhao.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zzhao.gmall.bean.PmsProductSaleAttr;
import com.zzhao.gmall.bean.PmsSkuImage;
import com.zzhao.gmall.bean.PmsSkuInfo;
import com.zzhao.gmall.bean.PmsSkuSaleAttrValue;
import com.zzhao.gmall.service.SkuService;
import com.zzhao.gmall.service.SpuService;
import com.zzhao.gmall.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author Administrator
 * @date 2019/11/5 0005下午 20:28
 */
@Controller
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;

    @Reference
    TestService testService;

    @Qualifier("mainThreadPoolExecutor")
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;


    @RequestMapping("{skuId}.html")
    public String index(@PathVariable String skuId, ModelMap modelMap) {
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        modelMap.put("skuInfo", pmsSkuInfo);
        //销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(), pmsSkuInfo.getId());
        modelMap.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrs);
        //销售属性hash表的制作
        Map<String, String> skuMap = new HashMap<String, String>();
        List<PmsSkuInfo> skuInfos = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
        skuInfos.forEach(pmsSkuInfo1 -> {
            String v = pmsSkuInfo1.getId();
            String k = "";
            for (PmsSkuSaleAttrValue skuSaleAttrValue : pmsSkuInfo1.getSkuSaleAttrValueList()) {
                k += skuSaleAttrValue.getSaleAttrValueId() + "|";
            }
            if (k.lastIndexOf("|") == k.length() - 1)
                k = k.substring(0, k.length() - 1);
            skuMap.put(k, v);
        });
        String skuMapStr = JSON.toJSONString(skuMap);
        modelMap.put("skuSaleAttrHashJsonStr", skuMapStr);
        return "item";
    }

    @RequestMapping("/sync/{skuId}.html")
    @ResponseBody
    public String sync(@PathVariable String skuId) {
        Long startTime = System.currentTimeMillis();
        int i=1/0;
        List<PmsSkuImage> pmsSkuImageList = skuService.getPmsSkuImages(skuId);
        PmsSkuInfo pmsSkuInfo = skuService.getPmsSkuInfo(skuId);
        System.out.println(pmsSkuInfo);
        System.out.println(pmsSkuImageList);
        Long endTime = System.currentTimeMillis();
        return String.valueOf((endTime - startTime));

    }


    @RequestMapping("/async/{skuId}.html")
    @ResponseBody
    public String async(@PathVariable String skuId) throws Exception {
        long startTime = System.currentTimeMillis();
        //阻塞，直到所有任务结束
        CompletableFuture<PmsSkuInfo> f1 = CompletableFuture.supplyAsync(() -> {
            return skuService.getPmsSkuInfo(skuId);
        }, threadPoolExecutor).whenComplete((r, e) -> {
            System.out.println("处理结果" + r);
            System.out.println("处理异常" + e);
        });
        CompletableFuture<List<PmsSkuImage>> f2 = CompletableFuture.supplyAsync(() -> {
            return skuService.getPmsSkuImages(skuId);
        }, threadPoolExecutor).whenComplete((r, e) -> {
            System.out.println("处理结果" + r);
            System.out.println("处理异常" + e);
        });
        //阻塞，直到所有任务结束
       CompletableFuture.allOf(f1, f2);
        System.out.println(f1.get());
        System.out.println(f2.get());
        long endTime = System.currentTimeMillis();
        return String.valueOf((endTime - startTime));
    }

    @RequestMapping("/test/{id}.html")
    @ResponseBody
    public String test(@PathVariable String id) {
        testService.insertBean();
        return "OK";
    }

    @RequestMapping("/test2/{id}.html")
    @ResponseBody
    public String test2(@PathVariable String id) {
        testService.select(id);
        return "OK";
    }
}
