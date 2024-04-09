package com.sky.service.user.Imp;

import com.sky.entity.Category;
import com.sky.mapper.UserCategoryMapper;
import com.sky.result.Result;
import com.sky.service.user.UserCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */
@RequiredArgsConstructor
@Service
public class UserCategoryServiceImp implements UserCategoryService {

    private final UserCategoryMapper userCategoryMapper;

    /**
     * 根据分类查询分类列表
     */
    @Override
    public Result<List<Category>> queryList(String type) {
        List<Category> categories = userCategoryMapper.queryList(type);

        return Result.success(categories);
    }
}
