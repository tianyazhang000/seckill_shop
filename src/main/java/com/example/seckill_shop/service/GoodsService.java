package com.example.seckill_shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill_shop.pojo.Goods;
import com.example.seckill_shop.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tyz
 * @since 2022-10-29
 */
public interface GoodsService extends IService<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
