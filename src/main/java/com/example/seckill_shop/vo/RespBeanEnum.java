package com.example.seckill_shop.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public enum RespBeanEnum {

    //通用
    SUCCESS(200,"登陆成功"),
    ERROR(500,"服务端异常"),
    //登录
    LOGIN_ERROR(530,"用户名或密码错误"),
    MOBILE_ERROR(531,"手机号码格式不正确"),
    BIND_ERROR(543,"参数校验异常"),
    MOBILE_NOT_EXIST(535,"手机号码不存在"),
    PASSWORD_UPDATE_FAIL(537,"密码更改失败"),
    SEESION_ERROR(540,"密码更改失败"),
    //秒杀模块
    EMPTY_STOCK(550,"库存不足"),
    REPEATE_ERROR(551,"该商品每人限购一件"),
    REQUEST_ILLEGAL(552, "请求非法，请重新尝试"),
    ERROR_CAPTCHA(553, "验证码错误，请重新输入"),
    ACCESS_LIMIT_REAHCED(554, "访问过于频繁，请稍后再试"),
    //订单模块5003xx
    ORDER_NOT_EXIST(565, "订单信息不存在")
    ;

    private final Integer code;
    private final String message;

}
