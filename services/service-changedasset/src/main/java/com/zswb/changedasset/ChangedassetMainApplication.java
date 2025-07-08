package com.zswb.changedasset;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zswb.changedasset.dao")
public class ChangedassetMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChangedassetMainApplication.class, args);
    }
}
