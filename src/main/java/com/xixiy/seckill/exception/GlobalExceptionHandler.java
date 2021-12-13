package com.xixiy.seckill.exception;


import com.xixiy.seckill.vo.RespBean;
import com.xixiy.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public RespBean ExceptionHandler(Exception e){
//        if (e instanceof GlobalException){
//            GlobalException ex = (GlobalException) e;
//            return RespBean.error(ex.getRespBeanEnum());
//        }else if (e instanceof BindException){
//            BindException bindException = (BindException) e;
//            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
//            respBean.setMsg("参数校验异常:" +bindException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
//            return respBean;
//        }
//        return RespBean.error(RespBeanEnum.ERROR);
//    }
}
