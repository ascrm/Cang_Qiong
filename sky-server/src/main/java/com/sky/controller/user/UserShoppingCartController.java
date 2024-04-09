package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.user.UserShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class UserShoppingCartController {

    @Autowired
    private UserShoppingCartService userShoppingCartService;

    /**
     * 查询购物车列表
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> queryList(){
        return userShoppingCartService.queryList();
    }

    /**
     * 添加菜品到购物车
     */
    @PostMapping("/add")
    public Result<String> addDish(@RequestBody ShoppingCartDTO shoppingCartDTO){

        return userShoppingCartService.addDish(shoppingCartDTO);
    }
}
