package com.wnsrich.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// lombok 提供的日志注解
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class application {
    public static void main(String[] args) {
        SpringApplication.run(application.class,args);
        log.info("START");
    }
}
