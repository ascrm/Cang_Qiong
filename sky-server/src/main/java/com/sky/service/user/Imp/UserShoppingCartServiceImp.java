package com.sky.service.user.Imp;

import cn.hutool.core.bean.BeanUtil;
import com.sky.dto.ShoppingCartDTO;
import com.sky.dto.SubDishDTO;
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
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtil.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(Long.parseLong(UserHolder.getUser()));

        ShoppingCart isShoppingCart = userShoppingCartMapper.queryShoppingCart(shoppingCart);

        //若不存在，则新增该菜品
        if(isShoppingCart==null){
            //查询dish表，获取shoppingCart其他的完整属性
            DishVO dish = dishMapper.selectById(shoppingCartDTO.getDishId());
            isShoppingCart = ShoppingCart.builder()
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
            userShoppingCartMapper.addShoppingCart(isShoppingCart);
            return Result.success();
        }

        //若存在，则使该菜品的number字段加1
        userShoppingCartMapper.addShoppingCartNum(isShoppingCart);

        return Result.success();
    }

    /**
     * 删除购物车中的菜品
     */
    @Override
    public Result<String> subDish(SubDishDTO subDishDTO) {
        userShoppingCartMapper.subDish(subDishDTO);
        return Result.success();
    }
}
