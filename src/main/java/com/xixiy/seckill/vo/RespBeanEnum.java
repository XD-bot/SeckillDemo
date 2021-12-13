package com.xixiy.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import sun.print.ServiceDialog;

@AllArgsConstructor
@ToString
@Getter
public enum RespBeanEnum {

    //通用
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),

    //登录模块
    LOGIN_ERROR(500210,"用户名或密码错误"),
    MOBILE_ERROR(500211,"手机号格式错误"),
    BIND_ERROR(500212,"参数校验异常"),
    UPDATE_PASSWORD_FAIL(500213,"更新密码失败"),
    MOBILE_NOT_EXIST(500214,"手机号码不存在"),
    USER_NO_EXIST(500215,"用户不能存在"),
    //秒杀模块
    EMPTY_STOCK(500501,"库存不足"),
    REPEATE_ERROR(500502,"不可重复抢购");

    private final Integer code;
    private final String msg;


}
