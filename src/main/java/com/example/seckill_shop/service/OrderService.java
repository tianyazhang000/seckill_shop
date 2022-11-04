package com.example.seckill_shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill_shop.pojo.Order;
import com.example.seckill_shop.pojo.User;
import com.example.seckill_shop.vo.GoodsVo;
import com.example.seckill_shop.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tyz
 * @since 2022-10-29
 */
public interface OrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);

    String createPath(User user, Long goodsId);

    boolean checkPath(User user, Long goodsId, String path);

    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
