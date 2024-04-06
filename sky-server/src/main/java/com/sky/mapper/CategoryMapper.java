package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/3/20
 */
@Mapper
public interface CategoryMapper {

    /**
     * 分页查询分类
     */
    List<Category> pageSelectCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改分类
     */
    @Update("update category set name=#{name}, sort=#{sort}, type=#{type}," +
            "update_time=#{updateTime},update_user=#{updateUser} where id=#{id}")
    @AutoFill(OperationType.UPDATE)
    void updateCategory(Category category);

    /**
     * 启用禁用分类
     */
    @Update("update category set status=#{status}," +
            "update_user=#{updateUser},update_time=#{updateTime} where id=#{id}")
    @AutoFill(OperationType.UPDATE)
    void updateCategoryStatus(Category category);

    /**
     * 新增分类
     */
    @Insert("insert into category values (#{id},#{type},#{name},#{sort},#{status}," +
            "#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(OperationType.INSERT)
    void addCategory(Category category);

    /**
     * 根据id删除分类
     */
    @Delete("delete from category where id=#{id}")
    void deleteCategory(Integer id);

    /**
     * 获取分类列表
     */
    @Select("select * from category")
    List<Category> getCategoryList();

}
