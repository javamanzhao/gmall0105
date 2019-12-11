package com.zzhao.gmall.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zzhao.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 * @date 2019/10/28 0028下午 17:32
 */
@Controller
public class UserController {

    @Reference
    UserService userService;


    @RequestMapping("/index")
    @ResponseBody
    public String index(){
        return "hello";
    }

    @RequestMapping("/getAllUser")
    @ResponseBody
    public Object getAllUser(){
        return userService.getAllUser();
    }

    @RequestMapping("/getUserAddressBymemberId")
    @ResponseBody
    public Object getUserAddressBymemberId(String memberId){
        return userService.getMemberReceiveAddressById(memberId);
    }
}
