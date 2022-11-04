package com.example.seckill_shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill_shop.pojo.SeckillOrder;
import com.example.seckill_shop.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tyz
 * @since 2022-10-29
 */
public interface SeckillOrderService extends IService<SeckillOrder> {

    Long getResult(User user, Long goodsId);
}
