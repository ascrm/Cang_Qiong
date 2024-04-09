package com.sky.service.user.Imp;

import com.sky.dto.ShoppingCartDTO;
import com.sky.mapper.DishMapper;
import com.sky.mapper.UserShoppingCartMapper;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.user.UserShoppingCartService;
import com.sky.utils.UserHolder;
import com.sky.vo.DishVO;
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
    private final DishMapper dishMapper;

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

        //查询数据库，判断该菜品或套餐是否存在(这个地方查要根据dto的三个字段来查，口味不一样要单独存为一个)
        ShoppingCart shoppingCart = userShoppingCartMapper.queryShoppingCart(shoppingCartDTO.getDishId());

        //TODO 若这里使套餐，查询的应该是setmeal表，而不是dish表
        //若不存在，则新增该菜品
        if(shoppingCart==null){
            //查询dish表，获取shoppingCart其他的完整属性
            DishVO dish = dishMapper.selectById(shoppingCartDTO.getDishId());
            shoppingCart = ShoppingCart.builder()
                    .amount(dish.getPrice())
                    .image(dish.getImage())
                    .name(dish.getName())
                    .userId(Long.parseLong(UserHolder.getUser()))
                    .number(1)
                    .setmealId(shoppingCartDTO.getSetmealId())
                    .dishId(shoppingCartDTO.getDishId())
                    .dishFlavor(shoppingCartDTO.getDishFlavor())
                    .build();

            //将菜品新增到购物车
            userShoppingCartMapper.addShoppingCart(shoppingCart);
            return Result.success();
        }

        //若存在，则使该菜品的number字段加1
        userShoppingCartMapper.addShoppingCartNum(shoppingCartDTO.getDishId());

        return Result.success();
    }
}
