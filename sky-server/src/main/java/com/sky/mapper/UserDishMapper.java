package com.sky.mapper;

import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */
@Mapper
public interface UserDishMapper  {

    /**
     * 根据分类id查询口味数据
     */
    @Select("select * from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> queryFlavorsByDishId(Long dishId);

    /**
     * 根据分类id查询菜品
     */
    List<DishVO> queryByCategoryId(Long categoryId);
}
