package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author:ascrm
 * @Date:2024/3/20
 */
@Service
public interface CategoryService {
    /**
     * 分页查询分类
     */
    Result<PageResult> pageSelectCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改分类
     */
    Result<String> updateCategory(CategoryDTO categoryDTO);

    /**
     * 启用禁用分类
     */
    Result<String> updateCategoryStatus(Integer status,Long id);

    /**
     * 新增分类
     */
    Result<String> addCategory(CategoryDTO categoryDTO);

    /**
     * 根据id删除分类
     */
    Result<String> deleteCategory(Integer id);

    /**
     * 获取分类列表
     */
    Result<List<Category>> getCategoryList();
}
