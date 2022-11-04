package com.example.seckill_shop.rabbitmq;

import com.example.seckill_shop.pojo.SeckillMessage;
import com.example.seckill_shop.pojo.SeckillOrder;
import com.example.seckill_shop.pojo.User;
import com.example.seckill_shop.service.GoodsService;
import com.example.seckill_shop.service.OrderService;
import com.example.seckill_shop.utils.JsonUtil;
import com.example.seckill_shop.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息消费者
 *
 * @author: tyz
 * @date 2022/3/7 7:44 下午
 * @ClassName: MQReceiver
 */
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;


    /**
     * 下单操作
     *
     * @param
     * @return void
     * @author tyz
     * @operation add
     * @date 6:48 下午 2022/3/8
     **/
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message) {
        log.info("接收消息：" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            return;
        }
        //判断是否重复抢购
        SeckillOrder SeckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (SeckillOrder != null) {
            return;
        }
        //下单操作
        orderService.seckill(user, goodsVo);
    }

}
