package com.wnsrich.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wnsrich.reggie.entity.Employee;
import com.wnsrich.reggie.mapper.EmployeeMapper;
import com.wnsrich.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
