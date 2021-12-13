package com.xixiy.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {

    private long code;
    private String msg;
    private Object object;

    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMsg(), null);
    }

    public static RespBean success(Object o){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMsg(), o);
    }

    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMsg(),null);
    }

    public static RespBean error(RespBeanEnum respBeanEnum,Object o){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMsg(),o);
    }
}
