package com.xixiy.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author xixiy
 * @since 2021-11-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID 手机号
     */
    private Long id;
    /**
     * 用户别名
     */
    private String nickname;

    /**
     * 密码 MD5(MD5(pass+slat)+slat)
     */
    private String password;

    /**
     * 盐值
     */
    private String slat;

    /**
     * 头像
     */
    private String head;

    /**
     * 注册时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerDate;

    /**
     * 最后一次登录时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 登录次数
     */
    private Long loginCount;


}
