package com.example.seckill_shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill_shop.exception.GlobalException;
import com.example.seckill_shop.pojo.User;
import com.example.seckill_shop.mapper.UserMapper;
import com.example.seckill_shop.service.UserService;
import com.example.seckill_shop.utils.CookieUtil;
import com.example.seckill_shop.utils.MD5Util;
import com.example.seckill_shop.utils.UUIDUtil;
import com.example.seckill_shop.vo.LoginVo;
import com.example.seckill_shop.vo.RespBean;
import com.example.seckill_shop.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tyz
 * @since 2022-10-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        User user = userMapper.selectById(mobile);
        if(user == null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if(!MD5Util.fromPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();
        //将用户信息存入redis中（序列化为二进制字节码）
        redisTemplate.opsForValue().set("user:" + ticket,user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    //根据cookie从redis中获取用户信息
    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if(user != null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }

    //更新密码
    @Override
    public RespBean updatePassword(String userTicket, String password,HttpServletRequest request,HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if(user == null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        int result = userMapper.updateById(user);
        if(result == 1){
            redisTemplate.delete("user" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }

}
