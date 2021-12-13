package com.xixiy.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;



public class MD5Utils {

    private static final String slat = "1a2b3c4d";

    //MD5 加密
    public static String md5en(String src){
        return DigestUtils.md5Hex(src);
    }

    //对前端接收的密码加密
    public static String inputPassToFormPass(String src){
        String str = "" + slat.charAt(0) + slat.charAt(3) + src + slat.charAt(2) + slat.charAt(4);
        return md5en(str);
    }

    //对存入数据库中的密码加密
    public static String formPassToDbPasss(String src, String slat){
        String str = "" + slat.charAt(0) + slat.charAt(3) + src + slat.charAt(2) + slat.charAt(4);
        return md5en(str);
    }

    //真正从前端到数据库的密码
    public static String inputPassToDbPass(String src,String slat){
        String formPass = inputPassToFormPass(src);
        String dbPass = formPassToDbPasss(formPass,slat);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(formPassToDbPasss("d82e94047344cfff6f8a76bdd0e0e6dd",slat));
        System.out.println(inputPassToDbPass("123456",slat));
    }


}
