package com.xixiy.seckill.vo;

import com.xixiy.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 商品返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {


    private Integer stockCount;

    private BigDecimal seckillPrice;

    private Date startDate;

    private Date endDate;

}
