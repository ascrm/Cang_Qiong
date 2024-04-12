package com.sky.mapper;

import com.sky.dto.SubDishDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

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
    void addShoppingCartNum(ShoppingCart shoppingCart);

    /**
     * 删除购物车中的菜品
     */
    boolean subDish(SubDishDTO subDishDTO);

    /**
     * 删除购物车中的菜品
     */
    void delShoppingCart(SubDishDTO subDishDTO);
}
