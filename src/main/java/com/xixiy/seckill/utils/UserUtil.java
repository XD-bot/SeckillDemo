package com.xixiy.seckill.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xixiy.seckill.pojo.User;
import com.xixiy.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成用户工具类
 */
public class UserUtil {

    public static void createUser(int count) throws Exception {


        List<User> userList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(13000000000L+i);
            user.setNickname("user"+i);
            user.setSlat("1a2b3c4d");
            user.setLoginCount(1L);
            user.setHead("123");
            user.setRegisterDate(new Date());
            user.setLastLoginTime(new Date());
            user.setPassword(MD5Utils.inputPassToDbPass("123456",user.getSlat()));
            userList.add(user);
        }
        System.out.println("create user");

        //获取数据库连接
        Connection conn = getConn();

        //插入数据
        String insertUserSql = "insert into t_user(login_count,nickname,register_date,slat,password,id,head,last_login_time) values (?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(insertUserSql);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            ps.setLong(1,user.getLoginCount());
            ps.setString(2,user.getNickname());
            ps.setTimestamp(3,new Timestamp(user.getRegisterDate().getTime()));
            ps.setString(4,user.getSlat());
            ps.setString(5,user.getPassword());
            ps.setLong(6,user.getId());
            ps.setString(7,user.getHead());
            ps.setTimestamp(8,new Timestamp(user.getLastLoginTime().getTime()));
            ps.addBatch();
        }
        ps.executeBatch();
        ps.clearParameters();
        conn.close();

        System.out.println("create user over");
        //登录，生成UserTicket
        String urlString = "http://localhost:8080/login/do_login";
        File file = new File("D:/dasi/秒杀/config.txt");
        if(file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for(int i=0;i<userList.size();i++) {
            User user = userList.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection)url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile="+user.getId()+"&password="+MD5Utils.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0 ,len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            System.out.println(response);
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("object");
            System.out.println("create token : " + user.getId());

            String row = user.getId()+","+token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();
        System.out.println("over");
    }

    public static void main(String[] args) throws Exception {
        createUser(1000);
    }
    private static  Connection getConn() throws Exception{

        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }
}
