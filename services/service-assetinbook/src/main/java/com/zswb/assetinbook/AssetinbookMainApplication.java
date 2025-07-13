package com.zswb.assetinbook;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {
        "com.zswb.assetinbook.service",
        "com.zswb.assetinbook.controller"
})
// 仅扫描业务组件（Service、Controller等）和convert包（MapStruct转换器）
@MapperScan("com.zswb.assetinbook.dao")
public class AssetinbookMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssetinbookMainApplication.class, args);
    }
}
