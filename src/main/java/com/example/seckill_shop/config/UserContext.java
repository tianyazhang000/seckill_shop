package com.example.seckill_shop.config;

import com.example.seckill_shop.pojo.User;
/**
 * 将线程与对象绑定，实现线程之间的数据隔离
 *
 * @author: tyz
 * @date 2022/3/8 5:24 下午
 * @ClassName: UserContext
 */
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();
    }

}
