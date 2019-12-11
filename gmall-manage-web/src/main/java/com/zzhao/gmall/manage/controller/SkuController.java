package com.zzhao.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zzhao.gmall.bean.PmsSkuInfo;
import com.zzhao.gmall.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 * @date 2019/11/5 0005下午 17:33
 */
@Controller
@CrossOrigin
public class SkuController {

    @Reference
    SkuService skuService;

    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public Object saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo)
    {
        return skuService.saveSkuInfo(pmsSkuInfo);
    }
}
