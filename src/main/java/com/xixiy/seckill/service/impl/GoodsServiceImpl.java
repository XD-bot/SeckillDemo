package com.xixiy.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xixiy.seckill.mapper.GoodsMapper;
import com.xixiy.seckill.pojo.Goods;
import com.xixiy.seckill.service.IGoodsService;
import com.xixiy.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xixiy
 * @since 2021-12-05
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
