package com.example.seckill_shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill_shop.mapper.OrderMapper;
import com.example.seckill_shop.mapper.SeckillOrderMapper;
import com.example.seckill_shop.pojo.SeckillOrder;
import com.example.seckill_shop.pojo.User;
import com.example.seckill_shop.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tyz
 * @since 2022-10-29
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if(seckillOrder != null){
            return seckillOrder.getOrderId();
        } else if (redisTemplate.hasKey("isStockEmpty:" + goodsId)) {
            return -1L;
        } else {
            return 0L;
        }
    }

}
