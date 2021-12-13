package com.xixiy.seckill.vo;

import com.xixiy.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailsVo {

    private User user;

    private GoodsVo goodsVo;

    private int remainSeconds;

    private int seckillStatus;

}
