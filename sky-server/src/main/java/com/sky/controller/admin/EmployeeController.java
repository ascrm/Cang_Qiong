package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.RedisKeyConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.EmployeeUpWord;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 员工登录
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        //将用户信息和token储存到redis中
        stringRedisTemplate.opsForValue().set(RedisKeyConstant.EMPLOYEE_TOKEN+token,employee.getId().toString());

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     * 分页查询员工
     */
    @GetMapping("/page")
    public Result<PageResult> pageSelectEmployee(EmployeePageQueryDTO employeePageQueryDTO){
        return employeeService.pageSelectEmployee(employeePageQueryDTO);
    }


    /**
     * 新增员工
     */
    @PostMapping
    public Result<String> addEmployee(@RequestBody EmployeeDTO employeeDTO){

        return employeeService.addEmployee(employeeDTO);
    }

    /**
     * 启用/禁用员工账号
     */
    @PostMapping("status/{status}")
    public Result<String> updateStatus(@PathVariable int status,Long id){
        return employeeService.updateStatus(status,id);
    }

    /**
     * 修改密码
     */
    @PutMapping("/editPassword")
    public Result<String> updatePassword(@RequestBody EmployeeUpWord employeeUpWord){
        return employeeService.updatePassword(employeeUpWord);
    }

    /**
     * 编辑员工信息
     */
    @PutMapping
    public Result<String> EditEmployeeInfo(@RequestBody EmployeeDTO employeeDTO){
        return employeeService.EditEmployeeInfo(employeeDTO);
    }
}
