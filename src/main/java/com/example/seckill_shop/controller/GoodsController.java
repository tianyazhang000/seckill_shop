package com.example.seckill_shop.controller;

import com.example.seckill_shop.pojo.User;
import com.example.seckill_shop.service.GoodsService;
import com.example.seckill_shop.vo.DetailVo;
import com.example.seckill_shop.vo.GoodsVo;
import com.example.seckill_shop.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 商品控制层 主要负责提供商品List，以及每件商品的详情信息
 *
 * @author: tyz
 * @date 2022/3/8 5:24 下午
 * @ClassName: GoodsController
 */
@Controller
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 跳转到商品页面
     * 压测数据2286.6/sec
     */
    @RequestMapping(value = "/toList",produces = "text/html;character=utf8",method = RequestMethod.GET)
    @ResponseBody
    public String toList(Model model, HttpServletRequest request, HttpServletResponse response, User user){
        if(user == null){
            return "login";
        }
        //序列化
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        //如果为空，手动渲染，存入redis中
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    //跳转商品详情页
    @RequestMapping(value = "/detail/{goodsId}",method = RequestMethod.GET)
    @ResponseBody
    public RespBean toDetail(User user, @PathVariable Long goodsId){
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀还未开始
        if(nowDate.before(startDate)){
            remainSeconds = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        } else if (nowDate.after(endDate)) {
            //秒杀结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            seckillStatus = 1;
            remainSeconds = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSeckillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }

}
