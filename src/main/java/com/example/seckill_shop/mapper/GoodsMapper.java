package com.example.seckill_shop.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckill_shop.pojo.Goods;
import com.example.seckill_shop.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 * @author tyz
 *
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
	 * 功能描述: 获取商品列表
	 *
	 * @param:
	 * @return:
	 * @since: 1.0.0
	 * @Author:tyz
	 */
	List<GoodsVo> findGoodsVo();

	/**
	 * 功能描述: 获取商品详情
	 *
	 * @param:
	 * @return:
	 * @since: 1.0.0
	 * @Author:tyz
	 * @param goodsId
	 */
	GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
