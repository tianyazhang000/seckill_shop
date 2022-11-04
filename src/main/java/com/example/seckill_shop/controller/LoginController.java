package com.example.seckill_shop.controller;

import com.example.seckill_shop.service.UserService;
import com.example.seckill_shop.vo.LoginVo;
import com.example.seckill_shop.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登录控制层 主要负责跳转登陆界面，以及调用UserService层中的doLogin实现登录
 *
 * @author: tyz
 * @date 2022/3/8 5:24 下午
 * @ClassName: LoginController
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    //跳转登陆页面
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        return userService.doLogin(loginVo,request,response);
    }

}
