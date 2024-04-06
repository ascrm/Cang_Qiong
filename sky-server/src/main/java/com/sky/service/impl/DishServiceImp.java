package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

import static com.sky.constant.MessageConstant.CATEGORY_EXIST;
import static com.sky.constant.RedisKeyConstant.DISH_CATEGORY_ID;
import static com.sky.constant.RedisKeyConstant.DISH_NAME;
import static com.sky.constant.RedisValueConstant.PRESENT;

/**
 * @Author:ascrm
 * @Date:2024/3/23
 */
@Service
public class DishServiceImp implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询菜品
     */
    @Override
    public Result<PageResult> pageSelectDish(DishPageQueryDTO dishPageQueryDTO) {

        //设置分页参数
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        //执行查询
        List<DishVO> list = dishMapper.pageSelect(dishPageQueryDTO);
        Page<DishVO> page = (Page<DishVO>) list;

        //获取分页信息
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());

        return Result.success(pageResult);
    }

    /**
     * 根据id查询菜品
     */
    @Override
    public Result<DishVO> selectById(Integer id) {
        DishVO dishVO = dishMapper.selectById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     */
    @Override
    public Result<String> updateDish(DishDTO dishDTO) {
        /*
        不需要判断是否存在，若存在则直接捕获异常就可以了
        原因新增菜品
         */

        /*
        因为把菜品信息保存到了缓存中，所以这里要保持数据一致，同时修改缓存中菜品的数据
         */


        //转化类型，因为AOP会切入updateDish这个方法，参数要与AOP中获取的参数保持一致
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);
        return Result.success();
    }

    /**
     * 新增菜品
     */
    @Override
    public Result<String> addDish(DishDTO dishDTO) {

        //bean对象转json
        String jsonDishDTO = JSONUtil.toJsonStr(dishDTO);

        //考虑到用户端的高并发访问菜品，所以这里把它存入redis中
        Boolean flag = stringRedisTemplate.opsForHash().putIfAbsent(DISH_CATEGORY_ID + dishDTO.getCategoryId(), dishDTO.getName(), jsonDishDTO);

        /*
        判断菜品是否存在有两种办法：
        1.查询数据库（效率太低，尽管新增菜品这个操作不存在高并发的情况，但还是不建议）
        2.查询redis（因为前面用的是hash结构储存菜品，所以不方便判断菜品name属性的唯一性）
        综上，这里还是选择用异常处理
         */

        //添加商品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.addDish(dish);

        return Result.success();
    }

    /**
     * 批量删除菜品
     */
    @Override
    public Result<String> deleteDishes(ArrayList<String> list) {
        //删除数据库
        dishMapper.deleteDishes(list);

        //加上key前缀
        for (String s : list) {
            s+=DISH_NAME;
        }
        //删除缓存
        stringRedisTemplate.delete(list);

        return Result.success();
    }

    /**
     * 菜品的起售和停售
     */
    @Override
    public Result<String> updateStatus(Integer status,Integer id) {
        dishMapper.updateStatus(status,id);
        return Result.success();
    }
}
