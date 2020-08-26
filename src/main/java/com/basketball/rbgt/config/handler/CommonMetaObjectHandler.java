package com.basketball.rbgt.config.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @title:
 * @description: 数据源新增修改
 * @copyright: Copyright (c) 2020
 * @company: 厦门宜车时代信息技术有限公司
 * @version: 1.0
 * @author: 俞春旺
 * @date: 2020-04-23
 */
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        this.setCreatorCode(metaObject);
        this.setModifierCode(metaObject);
        this.setCreateTime(metaObject);
        this.setUpdateTime(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setModifierCode(metaObject);
        this.setUpdateTime(metaObject);
    }

    /**
     * 设置入修改人编码
     *
     * @param metaObject 元数据
     */
    private void setModifierCode(MetaObject metaObject) {
        String operator = this.getOperator();
        this.setFieldData("lastModifiedBy", operator, metaObject);
    }

    /**
     * 设置创建人编码
     *
     * @param metaObject 元数据
     */
    private void setCreatorCode(MetaObject metaObject) {
        String operator = this.getOperator();
        this.setFieldData("createdBy", operator, metaObject);
    }


    /**
     * 设置更新时间
     *
     * @param metaObject 元数据
     */
    private void setUpdateTime(MetaObject metaObject) {
        this.setFieldData("updateTime", LocalDateTime.now(), metaObject);
    }

    /**
     * 设置创建时间
     *
     * @param metaObject 元数据
     */
    private void setCreateTime(MetaObject metaObject) {
        this.setFieldData("createTime", LocalDateTime.now(), metaObject);
    }


    /**
     * 设置字段数据
     *
     * @param fieldName  字段名
     * @param fieldVal   字段值
     * @param metaObject 元数据
     */
    private void setFieldData(String fieldName, Object fieldVal, MetaObject metaObject) {
        if (!this.hasField(fieldName, metaObject)) {
            return;
        }
        this.setFieldValByName(fieldName, fieldVal, metaObject);
    }

    /**
     * 是否有该字段
     *
     * @param fieldName  字段名称
     * @param metaObject 元数据
     * @return .
     */
    private boolean hasField(String fieldName, MetaObject metaObject) {
        return metaObject.hasGetter(fieldName) || metaObject.hasGetter(Constants.ENTITY_DOT + fieldName);
    }

    /**
     * 获取操作人编码
     *
     * @return 如果没有操作人，则返回-1
     */
    private String getOperator() {
        return "俞春旺";
    }
}
