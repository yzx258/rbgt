package com.basketball.rbgt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 篮球服务启动类
 */
@SpringBootApplication
@MapperScan("com.basketball.rbgt.mapper")
public class RbgtApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbgtApplication.class, args);
    }

}
