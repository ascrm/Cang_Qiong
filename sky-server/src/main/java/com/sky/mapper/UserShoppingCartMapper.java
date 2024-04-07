package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @Author:ascrm
 * @Date:2024/4/7
 */
@Mapper
public interface UserShoppingCartMapper {
    /**
     * 查询购物车列表
     */
    @Select("select * from shopping_cart where user_id=#{userId}")
    List<ShoppingCart> queryListByUserId(Long userId);

    /**
     * 添加菜品到购物车
     */
    @Insert("insert into shopping_cart values (#{id},#{name},#{image},#{userId},#{dishId},#{setmealId}," +
            "#{dishFlavor},#{number},#{amount},#{createTime})")
    void addDish(ShoppingCart shoppingCart);
}
