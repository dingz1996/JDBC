package com.atguigu.util3;

import com.atguigu.connectin1.ConnectionTest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//操作数据库的工具类
public class JDBCUtils {

    //获取数据库连接
     public static Connection getConnection() throws Exception {
         //1.读取配置文件中的4个基本信息  这里的类加载器是Application ClassLoader
         //默认识别路径是src下
         //生成一个IO流
         InputStream is =  ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
         Properties properties = new Properties();
         properties.load(is);
         String user = properties.getProperty("user");
         String url = properties.getProperty("url");
         String password = properties.getProperty("password");
         String driveClass = properties.getProperty("driveClass");

         //2.加载驱动
         Class.forName(driveClass);

         //3.获取连接
         Connection connection = DriverManager.getConnection(url, user, password);
         return connection;
     }

     //关闭连接和statement  statement：一个邮差
    public static void closeResource(Connection connection, PreparedStatement ps){
         try {
             if(connection!=null)
                 connection.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
         try{
             if(ps!=null)
                 ps.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
    }
  //关闭资源（多加了一个ResultSet）
    public static void closeResource(Connection connection, PreparedStatement ps, ResultSet resultSet){
        try {
            if(connection!=null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if(ps!=null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(resultSet!=null)
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
