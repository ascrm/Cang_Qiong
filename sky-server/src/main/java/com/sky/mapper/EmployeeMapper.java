package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.EmployeeUpWord;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 分页查询员工
     */
    List<Employee> pageSelectEmployee(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 新增员工
     */
    @Insert("insert into employee values (#{id},#{name},#{username},#{password},#{phone},#{sex}," +
            "#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(OperationType.INSERT)
    void addEmployee(Employee employee);

    /**
     * 修改员工状态
     */
    @Update("update employee set status=#{status},update_user=#{updateUser},update_time=#{updateTime} where id=#{id}")
    @AutoFill(OperationType.UPDATE)
    void updateStatus(Employee employee);

    /**
     * 根据id和旧密码查询用户
     */
    @Select("select * from employee where id=#{empId} and password=#{oldPassword}")
    Employee selectByPassword(EmployeeUpWord employeeUpWord);

    /**
     * 修改密码
     */
    @Update("update employee set password=#{newPassword} where id=#{empId}")
    @AutoFill(OperationType.UPDATE)
    void updatePassword(EmployeeUpWord employeeUpWord);

    /**
     * 编辑员工信息
     */
    @Update("update employee set id_number=#{idNumber}, " +
            "name=#{name}, phone=#{phone}, sex=#{sex}, username=#{username}," +
            "update_time=#{updateTime},update_user=#{updateUser} " +
            "where id=#{id}")
    @AutoFill(OperationType.UPDATE)
    void EditEmployeeInfo(Employee employee);
}
