package com.sky.service.user.Imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.user.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:ascrm
 * @Date:2024/4/7
 */

@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService {

    /**
     * 微信官方服务端地址，用于获取openid
     */
    public static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";

    private final WeChatProperties weChatProperties;

    private final UserMapper userMapper;

    /**
     * 用户登录
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {

        //用于获取用户唯一标识openid
        Map<String, String> map=new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",userLoginDTO.getCode());
        map.put("grant_type","authorization_code");

        //向微信服务端发送请求并获取响应
        String jsonString = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String openId = jsonObject.getString("openid");

        //先判断openId是否获取成功
        //获取失败，则抛出异常
        if(openId==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //根据openId查询数据库,判断用户是否是新用户
        //用户不存在,说明是新用户，则插入新用户到数据库中
        User user = userMapper.loginSelect(openId);
        if(user==null) {
            user = User.builder().openid(openId).createTime(LocalDateTime.now()).build();
            userMapper.loginInsert(user);
        }

        return user;
    }
}
