package com.xixiy.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.xixiy.seckill.pojo.Order;
import com.xixiy.seckill.pojo.SeckillOrder;
import com.xixiy.seckill.pojo.User;
import com.xixiy.seckill.service.IGoodsService;
import com.xixiy.seckill.service.IOrderService;
import com.xixiy.seckill.service.ISeckillGoodsService;
import com.xixiy.seckill.service.ISeckillOrderService;
import com.xixiy.seckill.vo.GoodsVo;
import com.xixiy.seckill.vo.RespBean;
import com.xixiy.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private IGoodsService goodsService;


    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/doSeckill2",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill2(User user, Long goodsId){
        // 未登录
        if (null == user){
            return RespBean.error(RespBeanEnum.USER_NO_EXIST);
        }

        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断商品库存
        if (goods.getStockCount() < 1){
            return  RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //判断是否重复抢购
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",
//                user.getId()).eq("goods_id",goodsId));

         SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goods.getId());
        if (seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        //真正执行秒杀，生成订单。
        Order order = orderService.seckill(user,goods);

        return RespBean.success(order);
    }
    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user, Long goodsId){
        // 未登录
        if (null == user){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断商品库存
        if (goods.getStockCount() < 1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMsg());
            return  "seckill_fail";
        }

//        //判断是否重复抢购
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",
//                user.getId()).eq("goods_id",goodsId));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goods.getId());
        if (seckillOrder != null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMsg());
            return "seckill_fail";
        }

        //真正执行秒杀，生成订单。
        Order order = orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "order_detail";
    }

}
