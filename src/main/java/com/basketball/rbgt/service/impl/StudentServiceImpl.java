package com.basketball.rbgt.service.impl;

import com.basketball.rbgt.entity.Student;
import com.basketball.rbgt.mapper.StudentMapper;
import com.basketball.rbgt.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 俞春旺
 * @since 2020-06-02
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
