package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.user.UserDishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/dish")
@Slf4j
public class UserDishController {

    private final UserDishService userDishService;

    /**
     * 根据分类id查询菜品
     */
    @GetMapping("/list")
    public Result<List<DishVO>> queryByCategoryId(Long categoryId){
        return userDishService.queryByCategoryId(categoryId);
    }
}
