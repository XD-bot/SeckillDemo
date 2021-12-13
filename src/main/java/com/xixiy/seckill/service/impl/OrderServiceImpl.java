package com.xixiy.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xixiy.seckill.mapper.OrderMapper;
import com.xixiy.seckill.pojo.*;
import com.xixiy.seckill.service.IGoodsService;
import com.xixiy.seckill.service.IOrderService;
import com.xixiy.seckill.service.ISeckillGoodsService;
import com.xixiy.seckill.service.ISeckillOrderService;
import com.xixiy.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xixiy
 * @since 2021-12-05
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goods) {

        //商品库存-1
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
//        seckillGoodsService.updateById(seckillGoods);
        //判断库存stock_count是否大于0，大于0才更新
        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count - 1 ")
        .eq("goods_id",goods.getId()).gt("stock_count",0));

        if (!result){
            return null;
        }
        Goods goods1 = goodsService.getOne(new QueryWrapper<Goods>().eq("id", goods.getId()));
        goods1.setGoodsStock(goods1.getGoodsStock() - 1);
        goodsService.update(new UpdateWrapper<Goods>().setSql("goods_stock = goods_stock - 1")
                .eq("id",goods1.getId()).gt("goods_stock",0));


        //生成订单
        Order order = new Order();

        order.setUesrId(user.getId());
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setDeliveryAddrId(0L);
        order.setCreateDate(new Date());

        orderMapper.insert(order);

        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());


        seckillOrderService.save(seckillOrder);

        redisTemplate.opsForValue().set("order:"+order.getUesrId() + ":" + order.getGoodsId(),seckillOrder);

        return order;
    }
}
