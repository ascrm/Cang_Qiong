package com.sky.mapper;

import com.sky.entity.User;
import com.sky.vo.UserLoginVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */
@Mapper
public interface UserMapper {

    @Select("select * from user")
    User loginSelect(String openid);

    void loginInsert(User user);

}
