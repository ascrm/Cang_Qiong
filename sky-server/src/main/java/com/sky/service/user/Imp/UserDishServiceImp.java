package com.sky.service.user.Imp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.entity.Dish;
import com.sky.mapper.UserDishMapper;
import com.sky.result.Result;
import com.sky.service.user.UserDishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        /*
        这里考虑到高并发的情况（是大概率存在的），先从redis中查询，若未查到，再查找数据库
         */
        String dishListJson = stringRedisTemplate.opsForValue().get(DISH_CATEGORY_ID + categoryId);
        if(dishListJson!=null){
            log.info("打印一下转json转化之前：{}",dishListJson);
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<DishVO> list = null;

            try {
                list = objectMapper.readValue(dishListJson, new TypeReference<ArrayList<DishVO>>() {});
            } catch (JsonProcessingException e) {

                throw new RuntimeException(e);
            }

            log.info("打印一下转化为集合的json：{}",list);
        }

       //若redis中未查询到，则从数据库中查询
        List<DishVO> list = userDishMapper.queryByCategoryId(categoryId);

        //然后将查询到的数据存入redis中
        String dishListStr = JSONUtil.toJsonStr(list);
        log.info("打印一下转化为json的集合：{}",dishListStr);
        stringRedisTemplate.opsForValue().set(DISH_CATEGORY_ID+categoryId,dishListStr);

        return Result.success(list);
    }
}
