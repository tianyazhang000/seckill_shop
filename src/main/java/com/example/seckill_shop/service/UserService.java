package com.example.seckill_shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill_shop.pojo.User;
import com.example.seckill_shop.vo.LoginVo;
import com.example.seckill_shop.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tyz
 * @since 2022-10-28
 */
public interface UserService extends IService<User> {

    //登录
    RespBean doLogin(LoginVo loginVo, HttpServletRequest resquest, HttpServletResponse response);

    //根据cookie获取用户信息
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    //更新密码
    RespBean updatePassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response);

}
