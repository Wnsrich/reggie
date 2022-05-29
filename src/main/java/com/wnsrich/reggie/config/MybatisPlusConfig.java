package com.wnsrich.reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus 配置类
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor MpInterceptor(){
        MybatisPlusInterceptor mpi = new MybatisPlusInterceptor();
        // 增加分页插件
        mpi.addInnerInterceptor(new PaginationInnerInterceptor());
        return mpi;
    }
}
