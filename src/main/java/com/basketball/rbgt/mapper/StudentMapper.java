package com.basketball.rbgt.mapper;

import com.basketball.rbgt.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 俞春旺
 * @since 2020-06-02
 */
public interface StudentMapper extends BaseMapper<Student> {

    List<Student> selectallll();

}
