package com.zzhao.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.zzhao.gmall.cart.mapper")
public class GmallCatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallCatServiceApplication.class, args);
    }

}
