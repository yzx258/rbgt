//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.basketball.rbgt.config.handler;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.baomidou.mybatisplus.core.toolkit.EnumUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @title: 自定义枚举类转换器
 * @description: 枚举类的字段名为code可以自动转换，如果需要自己指定字段，使用注解{@link EnumValue}
 * @copyright: Copyright (c) 2020
 * @company: 厦门宜车时代信息技术有限公司
 * @version: 1.0
 * @author: 俞春旺
 * @date: 2020-04-26
 */
@Slf4j
public class MybatisCustomEnumTypeHandler<E extends Enum<?>> extends BaseTypeHandler<Enum<?>> {
    private static final Map<Class<?>, Method> TABLE_METHOD_OF_ENUM_TYPES = new ConcurrentHashMap<>();
    private final Class<E> type;
    private final Method method;

    public MybatisCustomEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("类型参数不能为空");
        } else {
            this.type = type;
            if (IEnum.class.isAssignableFrom(type)) {
                try {
                    this.method = type.getMethod("getValue");
                } catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException(String.format("找不到指定的方法：%s.getValue()", type.getName()));
                }
            } else {
                Field field = dealEnumType(this.type);
                if (field == null) {
                    this.method = null;
                    return;
                }
                Method method = null;
                try {
                    method = ReflectionKit.getMethod(this.type, field);
                    TABLE_METHOD_OF_ENUM_TYPES.putIfAbsent(type, method);
                } catch (Exception e) {
                    // ignore
                } finally {
                    this.method = method;
                }

            }

        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Enum<?> parameter, JdbcType jdbcType) throws SQLException {
        try {
            this.method.setAccessible(true);
            if (jdbcType == null) {
                ps.setObject(i, this.method.invoke(parameter));
            } else {
                ps.setObject(i, this.method.invoke(parameter), jdbcType.TYPE_CODE);
            }
        } catch (IllegalAccessException e) {
            log.error(String.format("无法将字符串的值[%s]设置到指定的类型", parameter));
        } catch (InvocationTargetException e) {
            throw ExceptionUtils.mpe("找不到指定的方法:[%s],原因:", this.type.getName(), e);
        }

    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return null == rs.getObject(columnName) && rs.wasNull() ? null : EnumUtils.valueOf(this.type, rs.getObject(columnName), this.method);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return null == rs.getObject(columnIndex) && rs.wasNull() ? null : EnumUtils.valueOf(this.type, rs.getObject(columnIndex), this.method);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null == cs.getObject(columnIndex) && cs.wasNull() ? null : EnumUtils.valueOf(this.type, cs.getObject(columnIndex), this.method);
    }

    public static Field dealEnumType(Class<?> clazz) {
        return clazz.isEnum() ? Arrays.stream(clazz.getDeclaredFields())
                .filter((field) -> "code".equals(field.getName()) || field.isAnnotationPresent(EnumValue.class))
                .findFirst().orElse(null) : null;
    }
}
