package com.zzhao.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zzhao.gmall.service.CatalogService;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 * @date 2019/10/30 0030下午 16:29
 */
@Controller
@CrossOrigin
public class CatalogController {

    @Reference
    CatalogService catalogService;

    @RequestMapping("/getCatalog1")
    @ResponseBody
    public Object getCatalog1(){
        return catalogService.getCatalog1();
    }

    @RequestMapping("/getCatalog2")
    @ResponseBody
    public Object getCatalog2(@RequestParam String catalog1Id){
        return catalogService.getCatalog2(catalog1Id);
    }

    @RequestMapping("/getCatalog3")
    @ResponseBody
    public Object getCatalog3(@RequestParam String catalog2Id){
        return catalogService.getCatalog3(catalog2Id);
    }
}
