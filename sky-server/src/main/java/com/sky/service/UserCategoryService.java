package com.sky.service;

import com.sky.entity.Category;
import com.sky.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */

@Service
public interface UserCategoryService {

    /**
     * 根据分类查询分类列表
     */
    public Result<List<Category>> queryList(String type);
}
