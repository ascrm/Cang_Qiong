package com.sky.service.user;

import com.sky.result.Result;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */
@Service
public interface UserDishService {

    /**
     * 根据分类id查询菜品列表
     */
    Result<List<DishVO>> queryByCategoryId(Long categoryId);
}
