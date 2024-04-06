package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/3/23
 */
@Service
public interface DishService {

    /**
     * 分页查询菜品
     */
    Result<PageResult> pageSelectDish(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     */
    Result<DishVO> selectById(Integer id);

    /**
     * 修改菜品
     */
    Result<String> updateDish(DishDTO dishDTO);

    /**
     * 新增菜品
     */
    Result<String> addDish(DishDTO dishDTO);

    /**
     * 批量删除菜品
     */
    Result<String> deleteDishes(ArrayList<String> list);

    /**
     * 菜品的起售和停售
     */
    Result<String> updateStatus(Integer status,Integer id);
}
