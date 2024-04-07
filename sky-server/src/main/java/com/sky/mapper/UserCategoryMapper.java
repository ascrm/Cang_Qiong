package com.sky.mapper;

import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */
@Mapper
public interface UserCategoryMapper {

    /**
     * 根据分类查询分类列表
     */
    @GetMapping
    List<Category> queryList(String type);
}
