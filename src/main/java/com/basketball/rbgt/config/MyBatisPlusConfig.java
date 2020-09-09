package com.basketball.rbgt.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yiautos
 */
@MapperScan("com.basketball.rbgt.mapper")
@EnableTransactionManagement
@Configuration
public class MyBatisPlusConfig {
    /**
     * 注册乐观锁插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

//    /**
//     * 性能分析插件
//     */
//    @Bean
//    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
//        return new PerformanceMonitorInterceptor();
//    }

}
