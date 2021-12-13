package com.xixiy.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xixiy.seckill.pojo.Goods;
import com.xixiy.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xixiy
 * @since 2021-12-05
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
