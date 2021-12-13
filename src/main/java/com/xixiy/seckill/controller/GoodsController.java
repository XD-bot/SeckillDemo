package com.xixiy.seckill.controller;


import com.xixiy.seckill.pojo.User;
import com.xixiy.seckill.service.IGoodsService;
import com.xixiy.seckill.service.IUserService;
import com.xixiy.seckill.vo.DetailsVo;
import com.xixiy.seckill.vo.GoodsVo;
import com.xixiy.seckill.vo.RespBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.IconUIResource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model,User user,
                         HttpServletRequest request, HttpServletResponse response){

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String)valueOperations.get("goodsList");

        if (!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        //手动渲染html，存入redis并返回
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);

        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail2(Model model, User user, @PathVariable Long goodsId){

        GoodsVo goodVo = goodsService.findGoodsVoByGoodsId(goodsId);

        Date startDate = goodVo.getStartDate();
        Date endDate = goodVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀未开始
        if (nowDate.before(startDate)){
            remainSeconds  = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        }else if (nowDate.after(endDate)){
            //秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else{
            seckillStatus = 1;
            remainSeconds = 0;
        }

        DetailsVo detail = new DetailsVo();
        detail.setUser(user);
        detail.setGoodsVo(goodVo);
        detail.setRemainSeconds(remainSeconds);
        detail.setSeckillStatus(seckillStatus);

        return RespBean.success(detail);
    }

    @RequestMapping(value = "/toDetail2/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(Model model, User user, @PathVariable Long goodsId,
                           HttpServletRequest request, HttpServletResponse response){

        ValueOperations valueOperations = redisTemplate.opsForValue();

        String detailHtml = (String) valueOperations.get("goodsDetail:" + goodsId);

        if (!StringUtils.isEmpty(detailHtml)){
            return detailHtml;
        }

        model.addAttribute("user", user);
        GoodsVo goodVo = goodsService.findGoodsVoByGoodsId(goodsId);

        Date startDate = goodVo.getStartDate();
        Date endDate = goodVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀未开始
        if (nowDate.before(startDate)){
            remainSeconds  = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        }else if (nowDate.after(endDate)){
            //秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else{
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("seckillStatus",seckillStatus);
        model.addAttribute("goods",goodVo);
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());

        detailHtml = thymeleafViewResolver.getTemplateEngine().process("goods_detail",context);

        if (!StringUtils.isEmpty(detailHtml)){
            valueOperations.set("goodsDetail:" + goodsId, detailHtml,60,TimeUnit.SECONDS);
        }
        return detailHtml;
    }
}
