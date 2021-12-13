package com.xixiy.seckill.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtil {

    /*
     * 得到cookie值，不编码
     */

    public static String getCookieValue(HttpServletRequest request, String cookieName){
        return getCookieValue(request,cookieName,false);
    }

    /*
     * 得到cookie值
     */
    public static String getCookieValue(HttpServletRequest request,String cookieName,boolean isDecoder){
        Cookie[] cookieList = request.getCookies();
        if(cookieList == null || cookieName == null){//如果cookie里为空或者cookieName为空，返回空
            return null;
        }
        String retValue = null;
        try {


            for(int i=0;i<cookieList.length; i++){
                if(cookieList[i].getName().equals(cookieName)){
                    if(isDecoder){
                        retValue = URLDecoder.decode(cookieList[i].getValue(),"UTF-8");
                    }else{
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }


        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return retValue;
    }
    public static String getCookieValue(HttpServletRequest request, String cookieName,String encodeString){

        Cookie[] cookieList  = request.getCookies();
        if(cookieList == null || cookieName == null){
            return null;
        }

        String retValue = "";
        try{
            for(int i=0;i<cookieList.length;i++){
                if(cookieList[i].getName().equals(cookieName)){
                    retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
                    break;
                }
            }

        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return retValue;
    }
    /*
     * 设置cookie值   不设置生效时间默认浏览器关闭即失效，也不编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue){
        setCookie(request,response,cookieName,cookieValue,-1);
    }

    /*
     * 设置cookie值  指定时间生效，不 编码
     */
    public static void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,int cookieMaxage){
        setCookie(request,response,cookieName,cookieValue,cookieMaxage,false);
    }


    /*
     * 设置cookie值  不设置cookie生效时间，但编码
     */
    public static void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,boolean isEncode){
        setCookie(request,response,cookieName,cookieValue,-1,isEncode);
    }

    /**
     * 设置Cookie的值 在指定时间内生效, 编码参数
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,String cookieValue, int cookieMaxage, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
    }

    /*
     * 设置cookie值  在指定时间内生效，编码参数(指定编码参数)
     */
    public static void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,int cookieMaxage,String encodeString){
        doSetCookie(request,response,cookieName,cookieValue,cookieMaxage,encodeString);
    }

    /*
     * 删除cookie
     */
    public static void deleteCookie(HttpServletRequest request,HttpServletResponse response,String cookieName){
        doSetCookie(request,response,cookieName,"",-1,false);
    }


    /*
     * 设置cookie值。并使其在指定时间内有效
     *
     * cookieMaxage cookie生效的最大秒数
     */
    private static final void doSetCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,int cookieMaxage,boolean isEncode){

        try {
            if(cookieValue == null){
                cookieValue = "";
            }else if(isEncode){
                cookieValue = URLDecoder.decode(cookieValue,"UTF-8");
            }

            Cookie cookie = new Cookie(cookieName,cookieValue);

            if(cookieMaxage>0){
                cookie.setMaxAge(cookieMaxage);
            }
            if(null != request){//设置域名
                String domainName = getDomainName(request);
                System.out.println(domainName);
                if(!"localhost".equals(domainName)){
                    cookie.setDomain(domainName);
                }

                cookie.setPath("/");
                response.addCookie(cookie);
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    private static void doSetCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,int cookieMaxage,String encodeString){

        try {
            if(cookieValue == null){
                cookieValue = "";
            }else{
                cookieValue = URLEncoder.encode(cookieValue,encodeString);
            }

            Cookie cookie = new Cookie(cookieName,cookieValue);
            if (null !=request){
                String domainName = getDomainName(request);

                if(!"localhost".equals(domainName)){
                    cookie.setDomain(domainName);
                }
            }

            cookie.setPath("/");  //设置cookie在同一应用服务器下共享
            response.addCookie(cookie);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * 获取域名
     */
    public static final String getDomainName(HttpServletRequest request) {
        String domainName = null;

        String serverName = request.getRequestURL().toString();

        if (serverName == null || serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);//将http：//去掉
            final int end = serverName.indexOf("/");//获得第一个“/”的索引号
            serverName = serverName.substring(0, end);//例获得是localhost：8080 substring前包后不包

            //判断是否为域名，或者localhost之类
            final String[] domains = serverName.split("\\.");//以“.”进行分割

            int len = domains.length;

            if (len > 3) {
                //www.xxx.com.cn
                domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                //xxx.com or xxx.cn
                domainName = "." + domains[len - 2] + "." + domains[len - 1];
            } else {
                //有可能是localhost：8080这种形式，数组长度为0，直接把localhost：8080赋值给domainName
                domainName = serverName;
            }
        }
        //domainName 的值是localhost：8080，符合不为null并且包含“：”

        if (domainName != null && domainName.indexOf(":") > 0) {
            //按“：”分组，得到的数组是["localhost","8080"]
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        //返回localhost
        return domainName;
    }

}
