package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */
@Service
public interface UserShoppingCartService {

    /**
     * 查询购物车列表
     */
    Result<List<ShoppingCart>> queryList();

    /**
     * 添加菜品到购物车
     */
    Result<String> addDish(ShoppingCartDTO shoppingCartDTO);
}
