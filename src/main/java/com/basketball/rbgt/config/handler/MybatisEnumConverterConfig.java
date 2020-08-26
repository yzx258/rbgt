package com.basketball.rbgt.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @title: 自定义枚举类转换器配置
 * @description: 默认使用 {@link MybatisCustomEnumTypeHandler}
 * @copyright: Copyright (c) 2020
 * @company: 厦门宜车时代信息技术有限公司
 * @version: 1.0
 * @author: 俞春旺
 * @date: 2020-04-26
 */
@Configuration
@Slf4j
public class MybatisEnumConverterConfig {

    private String enumScanBasePackage = "com.example.mp.enums.**";
    private final SqlSessionFactory sqlSessionFactory;

    public MybatisEnumConverterConfig(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @PostConstruct
    public void inject() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter((metadataReader, metadataReaderFactory) -> "java.lang.Enum".equals(metadataReader.getClassMetadata().getSuperClassName()));
        Set<BeanDefinition> classes = provider.findCandidateComponents(enumScanBasePackage);
        if (CollectionUtils.isEmpty(classes)) {
            return;
        }
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();

        for (BeanDefinition beanDefinition : classes) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                typeHandlerRegistry.register(clazz, MybatisCustomEnumTypeHandler.class);
            } catch (ClassNotFoundException e) {
                log.error("初始化枚举转换器失败:" + e.getMessage());

            }
        }
    }

}
