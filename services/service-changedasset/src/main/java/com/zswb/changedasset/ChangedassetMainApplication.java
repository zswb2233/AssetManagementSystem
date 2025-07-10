package com.zswb.changedasset;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
// 仅扫描业务组件（Service、Controller等）和convert包（MapStruct转换器）
@ComponentScan(basePackages = {
        "com.zswb.changedasset.service",
        "com.zswb.changedasset.controller",
        "com.zswb.changedasset.convert" // 只扫描convert包的MapStruct接口
})
// 仅扫描MyBatis的DAO接口（dao包）
@MapperScan("com.zswb.changedasset.dao")
public class ChangedassetMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChangedassetMainApplication.class, args);
    }
}
