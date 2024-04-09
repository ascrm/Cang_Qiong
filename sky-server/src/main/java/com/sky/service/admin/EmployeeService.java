package com.sky.service.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.EmployeeUpWord;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import org.springframework.scheduling.support.SimpleTriggerContext;

public interface EmployeeService {

    /**
     * 员工登录
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 分页查询员工
     */
    Result<PageResult> pageSelectEmployee(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 新增员工
     */
    Result<String> addEmployee(EmployeeDTO employeeDTO);

    /**
     * 修改员工状态
     */
    Result<String> updateStatus(int status,Long id);

    /**
     * 修改员工密码
     */
    Result<String> updatePassword(EmployeeUpWord employeeUpWord);

    /**
     * 编辑员工信息
     */
    Result<String> EditEmployeeInfo(EmployeeDTO employeeDTO);
}
