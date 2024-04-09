package com.sky.service.admin.Imp;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sky.constant.StatusConstant.ENABLE;

/**
 * @Author:ascrm
 * @Date:2024/3/20
 */
@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询分类
     */
    @Override
    public Result<PageResult> pageSelectCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        //1.设置分页参数
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());

        //2.执行查询
        List<Category> list = categoryMapper.pageSelectCategory(categoryPageQueryDTO);
        Page<Category> records=(Page<Category>) list;

        PageResult pageResult=new PageResult();
        pageResult.setTotal(records.getTotal());
        pageResult.setRecords(records);

        return Result.success(pageResult);
    }

    /**
     * 修改分类
     */
    @Override
    public Result<String> updateCategory(CategoryDTO categoryDTO) {

        //类型转化，AOP需要捕获的参数是带有updateTime参数的Category实体类
        Category category = new Category();
        BeanUtil.copyProperties(categoryDTO,category,false);
        categoryMapper.updateCategory(category);
        return Result.success();
    }

    /**
     * 启用禁用分类
     */
    @Override
    public Result<String> updateCategoryStatus(Integer status,Long id) {

        //封装实体类，凡是用AOP切入的方法都需要让参数和切面类获取的参数对应
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);

        categoryMapper.updateCategoryStatus(category);
        return Result.success();
    }

    /**
     * 新增分类
     */
    @Override
    public Result<String> addCategory(CategoryDTO categoryDTO) {
        /*
         不需要判断，直接捕获异常，原因见其他的新增方法中
         */

        //不存在，则储存分类到数据库中
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setStatus(ENABLE);
        categoryMapper.addCategory(category);

        return Result.success();
    }

    /**
     * 根据id删除分类
     */
    @Override
    public Result<String> deleteCategory(Integer id) {
        //从数据库中删除分类
        categoryMapper.deleteCategory(id);
        return Result.success();
    }

    /**
     * 获取分类列表
     */
    @Override
    public Result<List<Category>> getCategoryList() {
        List<Category> list = categoryMapper.getCategoryList();
        return Result.success(list);
    }
}
