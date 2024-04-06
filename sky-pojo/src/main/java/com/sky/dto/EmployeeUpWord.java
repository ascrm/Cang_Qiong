package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:ascrm
 * @Date:2024/3/20
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpWord {
    private Integer empId;
    private String newPassword;
    private String oldPassword;
}
