package com.xixiy.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xixiy.seckill.exception.GlobalException;
import com.xixiy.seckill.pojo.User;
import com.xixiy.seckill.mapper.UserMapper;
import com.xixiy.seckill.service.IUserService;
import com.xixiy.seckill.utils.CookieUtil;
import com.xixiy.seckill.utils.MD5Utils;
import com.xixiy.seckill.utils.UUIDUtil;
import com.xixiy.seckill.vo.LoginVo;
import com.xixiy.seckill.vo.RespBean;
import com.xixiy.seckill.vo.RespBeanEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xixiy
 * @since 2021-11-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();


        User user = userMapper.selectById(mobile);
        if (null == user){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //校验密码
        if (!MD5Utils.formPassToDbPasss(password,user.getSlat()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:" + ticket, user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    @Override
    public User getUserByCookie(String ticket, HttpServletRequest request, HttpServletResponse response) {

        if (StringUtils.isEmpty(ticket)){
            return null;
        }
        User user = (User)redisTemplate.opsForValue().get("user:" + ticket);
        //重新设置cookie
        if (null != user){
            CookieUtil.setCookie(request,response,"userTicket",ticket);
        }
        return user;
    }

    @Override
    public RespBean updatePassword(String ticket, String password,HttpServletRequest request, HttpServletResponse response) {

        User user = getUserByCookie(ticket,request,response);
        if (user == null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        String newPassword = MD5Utils.inputPassToDbPass(password, user.getSlat());
        user.setPassword(newPassword);
        int result = userMapper.updateById(user);
        if (result == 1){
            redisTemplate.delete("user:"+ticket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.UPDATE_PASSWORD_FAIL);

    }
}
