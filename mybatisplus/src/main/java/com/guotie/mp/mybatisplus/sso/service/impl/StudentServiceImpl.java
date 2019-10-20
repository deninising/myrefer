package com.guotie.mp.mybatisplus.sso.service.impl;

import com.guotie.mp.mybatisplus.sso.entity.Student;
import com.guotie.mp.mybatisplus.sso.mapper.StudentMapper;
import com.guotie.mp.mybatisplus.sso.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dennis
 * @since 2019-10-13
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
