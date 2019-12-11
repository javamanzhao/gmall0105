package com.zzhao.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zzhao.gmall.bean.PmsBaseAttrInfo;
import com.zzhao.gmall.service.PmsBaseAttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 * @date 2019/10/31 0031上午 10:21
 */
@Controller
@CrossOrigin
public class BaseAttrController {

    @Reference
    PmsBaseAttrService pmsBaseAttrService;


    @RequestMapping("/attrInfoList")
    @ResponseBody
    public Object getAttrInfoList(@RequestParam String catalog3Id) {
        return pmsBaseAttrService.getAttrInfoList(catalog3Id);
    }

    @RequestMapping("/getAttrValueList")
    @ResponseBody
    public Object getAttrValueList(@RequestParam String attrId) {
        return pmsBaseAttrService.getAttrValueList(attrId);
    }

    @RequestMapping("/saveAttrInfo")
    @ResponseBody
    public Object saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {
        return pmsBaseAttrService.saveAttrInfo(pmsBaseAttrInfo);
    }
}
