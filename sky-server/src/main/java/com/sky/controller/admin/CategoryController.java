package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/3/20
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询分类
     */
    @GetMapping("/page")
    public Result<PageResult> pageSelectCategory(CategoryPageQueryDTO categoryPageQueryDTO){
        return categoryService.pageSelectCategory(categoryPageQueryDTO);
    }

    /**
     * 修改分类
     */
    @PutMapping
    public Result<String> updateCateGory(@RequestBody CategoryDTO categoryDTO){
        return categoryService.updateCategory(categoryDTO);
    }

    /**
     * 启用禁用分类
     */
    @PostMapping("/status/{status}")
    public Result<String> updateCategoryStatus(@PathVariable Integer status,Long id){
        return categoryService.updateCategoryStatus(status,id);
    }

    /**
     * 新增分类
     */
    @PostMapping
    public Result<String> addCategory(@RequestBody CategoryDTO categoryDTO){
        return categoryService.addCategory(categoryDTO);
    }

    /**
     * 根据id删除分类
     */
    @DeleteMapping
    public Result<String> deleteCategory(Integer id){
        return categoryService.deleteCategory(id);
    }

    /**
     * 获取分类列表
     */
    @GetMapping ("/list")
    public Result<List<Category>> getCategoryList(){
        return categoryService.getCategoryList();
    }
}
