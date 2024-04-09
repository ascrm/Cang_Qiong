package com.sky.service.user;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.springframework.stereotype.Service;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */

@Service
public interface UserService {

    User login(UserLoginDTO userLoginDTO);
}
