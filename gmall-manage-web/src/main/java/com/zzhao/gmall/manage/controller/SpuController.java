package com.zzhao.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zzhao.gmall.bean.PmsProductInfo;
import com.zzhao.gmall.manage.utils.PmsUploadUtil;
import com.zzhao.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 * @date 2019/11/4 0004下午 14:22
 */
@Controller
@CrossOrigin
public class SpuController {

    @Reference
    SpuService spuService;

    @RequestMapping("spuList")
    @ResponseBody
    public Object spuList(String catalog3Id) {
        return spuService.spuList(catalog3Id);
    }

    @RequestMapping("fileUpload")
    @ResponseBody
    public Object fileUpload(@RequestParam("file") MultipartFile multipartFile) {
        return PmsUploadUtil.uploadImage(multipartFile);
    }

    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public Object saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
        return spuService.saveSpuInfo(pmsProductInfo);
    }

    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public Object baseSaleAttrList(){
       return spuService.baseSaleAttrList();
    }


    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public Object spuSaleAttrList(String spuId){
        return spuService.spuSaleAttrList(spuId);
    }

    @RequestMapping("spuImageList")
    @ResponseBody
    public Object spuImageList(String spuId){
        return spuService.spuImageList(spuId);
    }
}
