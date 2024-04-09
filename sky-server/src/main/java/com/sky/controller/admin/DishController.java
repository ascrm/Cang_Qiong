package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @Author:ascrm
 * @Date:2024/3/22
 */
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 餐品分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> pageSelectDish(DishPageQueryDTO dishPageQueryDTO){
        return dishService.pageSelectDish(dishPageQueryDTO);
    }

    /**
     * 根据id查询菜品
     */
    @GetMapping("/{id}")
    public Result<DishVO> selectById(@PathVariable Long id){
        return dishService.selectById(id);
    }

    /**
     * 修改菜品
     */
    @PutMapping
    public Result<String> updateDish(@RequestBody DishDTO dishDTO){
        return dishService.updateDish(dishDTO);
    }

    /**
     * 新增菜品
     */
    @PostMapping
    public Result<String> addDish(@RequestBody DishDTO dishDTO){
        return dishService.addDish(dishDTO);
    }

    /**
     * 批量删除菜品
     */
    @DeleteMapping
    public Result<String> deleteDishes(@RequestParam("ids") ArrayList<String> list){
        return dishService.deleteDishes(list);
    }

    /**
     * 餐品的起售停售
     */
    @PostMapping("status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status,Integer id){
        return dishService.updateStatus(status,id);
    }
}