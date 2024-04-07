package com.sky.service.impl;

import com.sky.mapper.UserDishMapper;
import com.sky.result.Result;
import com.sky.service.UserDishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */

@RequiredArgsConstructor
@Service
public class UserDishServiceImp implements UserDishService {

    private final UserDishMapper userDishMapper;

    /**
     * 根据分类id查询菜品列表
     */
    @Override
    public Result<List<DishVO>> queryByCategoryId(Long categoryId) {
        List<DishVO> list = userDishMapper.queryByCategoryId(categoryId);
        return Result.success(list);
    }
}
