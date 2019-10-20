package com.guotie.mp.mybatisplus.sso.service.impl;

import com.guotie.mp.mybatisplus.sso.entity.Employee;
import com.guotie.mp.mybatisplus.sso.mapper.EmployeeMapper;
import com.guotie.mp.mybatisplus.sso.service.EmployeeService;
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
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
