package com.example.seckill_shop.controller;

import com.example.seckill_shop.config.AccessLimit;
import com.example.seckill_shop.exception.GlobalException;
import com.example.seckill_shop.pojo.SeckillMessage;
import com.example.seckill_shop.pojo.SeckillOrder;
import com.example.seckill_shop.pojo.User;
import com.example.seckill_shop.rabbitmq.MQSender;
import com.example.seckill_shop.service.GoodsService;
import com.example.seckill_shop.service.OrderService;
import com.example.seckill_shop.service.SeckillOrderService;
import com.example.seckill_shop.utils.JsonUtil;
import com.example.seckill_shop.vo.GoodsVo;
import com.example.seckill_shop.vo.RespBean;
import com.example.seckill_shop.vo.RespBeanEnum;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private RedisScript<Long> script;
    @Autowired
    private OrderService orderService;

    private Map<Long,Boolean> EmptyStockMap = new HashMap<>();

    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(@PathVariable String path, User user, Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SEESION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        boolean check = orderService.checkPath(user,goodsId,path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order" + user.getId() + ":" + goodsId);
        if(seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //内存标记 减少redis访问
        if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
        Long stock = (Long) redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if(stock < 0){
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //下单
        SeckillMessage seckillMessage = new SeckillMessage(user,goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
    }

    //获取秒杀结果，orderId 成功 -1 失败  排队中 0
    @RequestMapping(value = "/getResult",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SEESION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }

    //获取唯一地址码
    @AccessLimit(second = 5,maxCount = 5,needLogin = true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user,Long goodsId,String captcha){
        if(user == null){
            return RespBean.error(RespBeanEnum.SEESION_ERROR);
        }
        boolean check = orderService.checkCaptcha(user,goodsId,captcha);
        if(!check){
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(user,goodsId);
        return RespBean.success(str);
    }
    
    //
    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletRequest request, HttpServletResponse response){
        if(user == null || goodsId < 0){
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //设置请求头为输出图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pargam","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        //生成验证码 放入redis中
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId,captcha.text(),300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败",e.getMessage());
        }
    }

    //初始化,将商品库存数量加载到redis中
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo -> {
                    redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
                    EmptyStockMap.put(goodsVo.getId(),false);
                }
        );
    }
}
