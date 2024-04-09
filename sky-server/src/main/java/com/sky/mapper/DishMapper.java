package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/3/23
 */
@Mapper
public interface DishMapper {

    /**
     * 分页查询菜品
     */
    List<DishVO> pageSelect(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     */
    @Select("select * from dish where id = #{id}")
    DishVO selectById(Long id);

    /**
     * 修改菜品信息
     */
    @Update("update dish set name=#{name}, category_id=#{categoryId}, price=#{price}," +
            "image=#{image},description=#{description},status=#{status}," +
            "update_user=#{updateUser},update_time=#{updateTime} where id=#{id}")
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    /**
     * 新增菜品
     */
    @Insert("insert into dish values (#{id},#{name},#{categoryId},#{price},#{image}," +
            "#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(OperationType.INSERT)
    void addDish(Dish dish);

    /**
     * 批量删除菜品
     */
    void deleteDishes(ArrayList<String> list);

    /**
     * 菜品的起售和停售
     */
    @Update("update dish set status=#{status} where id=#{id} ")
    void updateStatus(Integer status,Integer id);


}
