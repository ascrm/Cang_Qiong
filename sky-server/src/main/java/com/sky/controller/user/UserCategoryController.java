package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.UserCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/category")
@Slf4j
public class UserCategoryController {

    private final UserCategoryService userCategoryService;

    /**
     * 根据分类查询分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> queryList(String type){
        return userCategoryService.queryList(type);
    }
}
