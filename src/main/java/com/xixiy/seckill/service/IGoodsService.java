package com.xixiy.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xixiy.seckill.pojo.Goods;
import com.xixiy.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xixiy
 * @since 2021-12-05
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 功能描述：获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
