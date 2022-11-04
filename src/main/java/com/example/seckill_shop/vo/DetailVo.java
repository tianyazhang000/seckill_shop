package com.example.seckill_shop.vo;

import com.example.seckill_shop.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {

    private User user;

    private GoodsVo goodsVo;

    private int seckillStatus;

    private int remainSeconds;

}
