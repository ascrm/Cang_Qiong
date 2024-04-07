package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.dto.ShoppingCartDTO;
import com.sky.mapper.UserShoppingCartMapper;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.UserShoppingCartService;
import com.sky.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */
@RequiredArgsConstructor
@Service
public class UserShoppingCartServiceImp implements UserShoppingCartService {

    private final UserShoppingCartMapper userShoppingCartMapper;

    /**
     * 查询购物车列表
     */
    @Override
    public Result<List<ShoppingCart>> queryList() {
        Long userId =Long.parseLong(UserHolder.getUser());
        List<ShoppingCart> shoppingCarts = userShoppingCartMapper.queryListByUserId(userId);
        return Result.success(shoppingCarts);
    }

    /**
     * 添加菜品到购物车
     */
    @Override
    public Result<String> addDish(ShoppingCartDTO shoppingCartDTO) {


        return null;
    }
}
