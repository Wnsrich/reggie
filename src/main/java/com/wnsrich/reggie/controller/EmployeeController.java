package com.wnsrich.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wnsrich.reggie.common.R;
import com.wnsrich.reggie.entity.Employee;
import com.wnsrich.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    /**
     * 登录方法
     *
     * @param request  将结果存到session里面去
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        // 获取前端传过来的密码并对其进行md5加密,用于和数据库已加密的密码进行匹配
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 按照username去查询，并将结果封装成一个employee对象
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(lqw);

        // 对返回结果做校验
        if (one == null)
            return R.error("用户不存在");

        if (!one.getPassword().equals(password))
            return R.error("密码不正确");

        if (one.getStatus() == 0)
            return R.error("用户被禁用");

        // 将查询结果存放在session中，返回成功
        request.getSession().setAttribute("employee", one.getId());

        return R.success(one);
    }

    /**
     * 退出功能
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增操作
     * @param employee
     * @param request
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        // 设置默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return R.success("新增成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        // 分页构造器
        Page pageInfo = new Page(page,pageSize);
        // 条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name)) {
            // 处理搜索框中带空格
            name = name.replaceAll(" ", "");
            lqw.like(Employee::getName, name).orderByDesc(Employee::getUpdateTime);
        }else{
            lqw.select().orderByDesc(Employee::getUpdateTime);
        }
        employeeService.page(pageInfo,lqw);


        return R.success(pageInfo);
    }

    /**
     *  修改员工状态 1表示正常 0表示禁用
     * @param employee
     * @param request
     * @return
     */
    @PutMapping
    private R<String> updateStatus(@RequestBody Employee employee,HttpServletRequest request){
        Long empId = (Long) request.getSession().getAttribute("employee");
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    /**
     * 按id查询employee
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> selectInfo(@PathVariable String id){
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        lqw.eq(Employee::getId,id);
        Employee one = employeeService.getOne(lqw);
        return R.success(one);
    }
}
