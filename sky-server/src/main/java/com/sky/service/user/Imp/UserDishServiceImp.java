package com.sky.service.user.Imp;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.entity.DishFlavor;
import com.sky.mapper.UserDishMapper;
import com.sky.result.Result;
import com.sky.service.user.UserDishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sky.constant.RedisKeyConstant.DISH_CATEGORY_ID;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDishServiceImp implements UserDishService {

    private final UserDishMapper userDishMapper;

    private final StringRedisTemplate stringRedisTemplate;


    /**
     * 根据分类id查询菜品列表
     */
    @Override
    public Result<List<DishVO>> queryByCategoryId(Long categoryId) {

        //先从redis中查询菜品
        String dishListJson = stringRedisTemplate.opsForValue().get(DISH_CATEGORY_ID + categoryId);

        //将集合字符串转化为对象
        if(dishListJson!=null){

            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<DishVO> list;
            try {
                list = objectMapper.readValue(dishListJson, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return Result.success(list);
        }

        //若redis中未查询到，则从数据库中查询
        //1.查询口味数据
        List<DishVO> list = userDishMapper.queryByCategoryId(categoryId);
        for (DishVO dishVO : list) {
            List<DishFlavor> dishFlavors = userDishMapper.queryFlavorsByDishId(dishVO.getId());
            dishVO.setFlavors(dishFlavors);
        }

        //然后将查询到的数据存入redis中
        String dishListStr = JSONUtil.toJsonStr(list);
        stringRedisTemplate.opsForValue().set(DISH_CATEGORY_ID+categoryId,dishListStr);

       return Result.success(list);
    }
}
