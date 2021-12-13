package com.xixiy.seckill.controller;


import com.xixiy.seckill.pojo.User;
import com.xixiy.seckill.vo.RespBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xixiy
 * @since 2021-11-30
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @RequestMapping("/info")
    @ResponseBody
    public RespBean userInfo(User user){

        return RespBean.success(user);
    }
}
