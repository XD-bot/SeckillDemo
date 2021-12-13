package com.xixiy.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xixiy.seckill.pojo.Order;
import com.xixiy.seckill.pojo.User;
import com.xixiy.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xixiy
 * @since 2021-12-05
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);
}
