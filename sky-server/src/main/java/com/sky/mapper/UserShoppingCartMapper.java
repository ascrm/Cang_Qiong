package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * @Author:ascrm
 * @Date:2024/4/7
 */
@Mapper
public interface UserShoppingCartMapper {
    /**
     * 根据用户id查询购物车列表
     */
    @Select("select * from shopping_cart where user_id=#{userId}")
    List<ShoppingCart> queryListByUserId(Long userId);

    /**
     * 根据菜品id查询菜品
     */
    ShoppingCart queryShoppingCart(ShoppingCart shoppingCart);

    /**
     * 添加菜品到购物车
     */
    @Insert("insert into shopping_cart values (#{id},#{name},#{image},#{userId},#{dishId},#{setmealId}," +
            "#{dishFlavor},#{number},#{amount},#{createTime})")
    void addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 菜品或套餐number字段加1
     */
    @Update("update shopping_cart set number=number+1 where dish_id=#{dishId} and user_id=#{userId}")
    void addShoppingCartNum(ShoppingCart shoppingCart);
}
