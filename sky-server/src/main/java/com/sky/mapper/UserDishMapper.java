package com.sky.mapper;

import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */
@Mapper
public interface UserDishMapper  {

    List<DishVO> queryByCategoryId(Long categoryId);
}
