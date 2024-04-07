package com.sky.controller.user;

import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import static com.sky.constant.RedisKeyConstant.SHOP_STATUS;

/**
 * @Author:ascrm
 * @Date:2024/3/20
 */
@RestController
@RequestMapping("/user/shop")
public class UserShopController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取商店status
     */
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        String status = stringRedisTemplate.opsForValue().get(SHOP_STATUS);
        assert status != null;
        return Result.success(Integer.parseInt(status));
    }

    /**
     * 修改商店status
     */
    @PutMapping("/{status}")
    public Result<String> updateStatus(@PathVariable String status){
        stringRedisTemplate.opsForValue().set(SHOP_STATUS,status);
        return Result.success();
    }
}
