package com.sky.service.admin.Imp;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.DishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.sky.constant.RedisKeyConstant.DISH_CATEGORY_ID;
import static com.sky.constant.RedisKeyConstant.DISH_NAME;

/**
 * @Author:ascrm
 * @Date:2024/3/23
 */
@RequiredArgsConstructor
@Service
public class DishServiceImp implements DishService {

    private final DishMapper dishMapper;

    private final StringRedisTemplate stringRedisTemplate;

    private static final ExecutorService EXECUTOR_SERVICE= Executors.newFixedThreadPool(8);


    /**
     * 分页查询菜品
     */
    @Override
    public Result<PageResult> pageSelectDish(DishPageQueryDTO dishPageQueryDTO) {

        //设置分页参数
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        //执行查询
        List<DishVO> list = dishMapper.pageSelect(dishPageQueryDTO);
        Page<DishVO> page = (Page<DishVO>) list;

        //获取分页信息
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());

        return Result.success(pageResult);
    }

    /**
     * 根据id查询菜品
     */
    @Override
    public Result<DishVO> selectById(Long id) {
        DishVO dishVO = dishMapper.selectById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     */
    @Override
    public Result<String> updateDish(DishDTO dishDTO) {
        /*
        不需要判断是否存在，若存在则直接捕获异常就可以了
         */

        /*
        error 错误
        因为把菜品信息保存到了缓存中，所以这里要保持数据一致，同时修改缓存中菜品的数据
         */

        /*
        correct 纠正
        1. 缓存更新策略中，修改修改数（新增数据也一样，但是新增数据并没有可删的缓存，所以不用管）据选择的缓存操作并不是更新缓存，
            而是删除缓存，因为某一个数据的 n次 更新，实际上只有最后一次更新是有效数据，中间并不需要频繁的更新缓存
        2. 但是修改了数据，数据库是必然需要同步修改的
         */

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);

        //删除缓存
        stringRedisTemplate.delete(DISH_CATEGORY_ID+dishDTO.getCategoryId());

        return Result.success();
    }

    /**
     * 新增菜品
     */
    @Override
    public Result<String> addDish(DishDTO dishDTO) {

        /*
        判断菜品是否存在有两种办法：
        1.查询数据库：效率太低，尽管新增菜品这个操作不存在高并发的情况，但还是不建议
        2.查询redis：考虑到后面的应用，这里选择set集合储存同一类菜品。所以用categoryId作为key，
            那么就不是很方便判断name属性是否存在了
            2.1.不方便的原因：因为这里用的是set集合，查出来是一个集合，又要从集合中找到name属性相同的菜品不太现实
        综上，这里还是选择用异常处理来解决重复存入相同菜品的问题
         */


        /*
        error 这一栏的想法全部错误
        1. 为什么要把菜品存入redis中：
            因为用户端大量用户同时使用小程序的时候，短时间内会有大量请求，频繁的操作数据库，使系统性能下降

        2. 为什么要选择set集合储存菜品：
            （1）string：用categoryId做key，如果后面有相同类型的新菜品添加，不能直接添加redis中
                （因为会覆盖与原来的数据），而要通过查询同一类菜品的列表，然后添加
            （2）hash：用categoryId和另外某一变量作为key，hashKey。获取到的是一个键值对的map集合，后续还要进行
                其他处理才能得到一个纯粹的菜品列表。可行但不方便

        3. 为什么要先操作数据库再存入redis：
            因为我这里选择的是用set集合储存菜品，假设储存的是相同name的菜品，只是其他的某个属性不一样，也可以存入
            redis中。但这样就存在了相同的菜品
            但是如果先操作数据库，即使存在上面的情况，也会发生异常（表name字段唯一），然后捕获异常并抛出，
            就不会再存入redis中
         */

        /*
        correct 纠正
        1. 这里在新增菜品的时候并不需要存入redis，新增跟修改其实是一类。存入redis的时机应该是在查询菜品的时候
        2. 应该就是用string类型存储缓存。因为存入redis的时机是查询菜品的时候，这个时候恰好能得到一个菜品列表，那么就作为整体
            直接存入redis就可以了。没必要用set（因为不需要一个菜品一个菜品的存），更不需要用hash（因为后面不需要单独操作某一个菜品，
            都是把一类的菜品作为整体操作）
        3. 对于缓存更新策略，先操作数据库，再更新缓存是正确的，但是并不是上面那个理由
         */

        //添加商品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.addDish(dish);

        return Result.success();
    }

    /**
     * 批量删除菜品
     */
    @Override
    public Result<String> deleteDishes(ArrayList<String> list) {
        //删除数据库
        dishMapper.deleteDishes(list);

        //加上key前缀
        for (String s : list) {
            s+=DISH_NAME;
        }
        //删除缓存
        stringRedisTemplate.delete(list);

        return Result.success();
    }

    /**
     * 菜品的起售和停售
     */
    @Override
    public Result<String> updateStatus(Integer status,Integer id) {
        dishMapper.updateStatus(status,id);
        return Result.success();
    }
}
