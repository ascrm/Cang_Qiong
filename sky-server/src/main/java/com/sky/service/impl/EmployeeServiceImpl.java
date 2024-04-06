package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.EmployeeUpWord;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.UserHolder;
import io.netty.util.ResourceLeakTracker;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.sky.constant.MessageConstant.EMPLOYEE_EXIST;
import static com.sky.constant.MessageConstant.PASSWORD_ERROR;
import static com.sky.constant.PasswordConstant.DEFAULT_PASSWORD;
import static com.sky.constant.RedisKeyConstant.EMPLOYEE_USERNAME;
import static com.sky.constant.RedisValueConstant.PRESENT;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 员工登录
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 分页查询员工
     */
    @Override
    public Result<PageResult> pageSelectEmployee(EmployeePageQueryDTO employeePageQueryDTO) {
        //1.设置分页参数
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        //2.执行查询
        List<Employee> employees = employeeMapper.pageSelectEmployee(employeePageQueryDTO);
        Page<Employee> page= (Page<Employee>) employees;

        //3.获取分页信息
        long total = page.getTotal();
        List<Employee> records = page.getResult();

        //4.封装数据
        PageResult pageResult=new PageResult();
        pageResult.setTotal(total);
        pageResult.setRecords(records);

        return Result.success(pageResult);
    }

    /**
     * 新增员工
     */
    @Override
    public Result<String> addEmployee(EmployeeDTO employeeDTO) {
        /*
        这里不判断是否已经存在改员工，原因见DishServiceImp]
        全局异常处理器直接捕获异常就行了
         */

        //添加新用户到数据库
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setPassword(DEFAULT_PASSWORD);
        employee.setStatus(StatusConstant.ENABLE);
        employeeMapper.addEmployee(employee);

        return Result.success();
    }

    /**
     * 修改员工状态
     */
    @Override
    public Result<String> updateStatus(int status,Long id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(status);
        employeeMapper.updateStatus(employee);
        return Result.success();
    }

    /**
     * 修改员工密码
     */
    @Override
    public Result<String> updatePassword(EmployeeUpWord employeeUpWord) {

        //存在则修改密码
        employeeMapper.updatePassword(employeeUpWord);
        return Result.success();
    }

    /**
     * 编辑员工信息
     */
    @Override
    public Result<String> EditEmployeeInfo(EmployeeDTO employeeDTO) {

        //需要转化类型，因为EmployeeDTO里面没有createTime等等属性
        Employee employee = new Employee();
        BeanUtil.copyProperties(employeeDTO,employee,false);

        employeeMapper.EditEmployeeInfo(employee);
        return Result.success();
    }
}
