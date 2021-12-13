package com.xixiy.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xixiy.seckill.pojo.User;
import com.xixiy.seckill.vo.LoginVo;
import com.xixiy.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xixiy
 * @since 2021-11-30
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String ticket,HttpServletRequest request, HttpServletResponse response);

    RespBean updatePassword(String ticket, String password,HttpServletRequest request, HttpServletResponse response);
}
