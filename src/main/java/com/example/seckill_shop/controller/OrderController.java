package com.example.seckill_shop.controller;


import com.example.seckill_shop.pojo.User;
import com.example.seckill_shop.service.OrderService;
import com.example.seckill_shop.vo.OrderDetailVo;
import com.example.seckill_shop.vo.RespBean;
import com.example.seckill_shop.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  下单控制层
 * </p>
 *
 * @author tyz
 * @since 2022-10-29
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user,Long orderId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SEESION_ERROR);
        }
        OrderDetailVo detail = orderService.detail(orderId);
        return RespBean.success(detail);
    }

}
