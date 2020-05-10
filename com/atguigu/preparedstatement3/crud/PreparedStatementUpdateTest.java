package com.atguigu.preparedstatement3.crud;
import com.atguigu.util3.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/*
使用Preparedstatement来替换Statement , 实现对数据表的增删改操作
 */
public class PreparedStatementUpdateTest {

     //向customers表中添加一条记录
    @Test
    public void testInsert() throws ClassNotFoundException {
        Class.forName("com.atguigu.connectin1.ConnectionTest");
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1.读取配置文件的4个基本信息
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
            connection = DriverManager.getConnection(url, user, password);

            //?是占位符
            //4.预编译sql语句，返回PreparedStatement实例
            String sql = "Insert into customers(name,email,birth)values(?,?,?)";
            ps = connection.prepareStatement(sql);

            //5.填充占位符
            //注意：这里的parameterIndex要从1开始  因为数据库的index是从1开始的 不要和数组弄混了
            //name的类型是String birth在数据库的类型是date  在java里面对应sql.date
            ps.setString(1,"金城武");
            ps.setString(2,"jcw@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //这个date是util下的date
            Date date = sdf.parse("1000-01-01");//1000/01-01没有按照上面yyyy-MM-dd的格式 所以需要catch ParseException
            ps.setDate(3,new java.sql.Date(date.getTime()));

            //6.执行操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源关闭：IO流需要关闭、socket需要关闭、连接需要关闭
            try {
                if(ps!=null)//这里的ps有可能是null：properties.load(is)这里报IOException错   所以还要加一层判断
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                //和上面同理
                if(connection!=null)
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testUpdate() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();

            //预编译sql语句 返回PreparedStatement的实例
            String sql = "UPDATE customers set name = ? where id = ?";
            //  Statement statement = connection.createStatement();  这个没有预编译 没有把sql传进去
            ps = connection.prepareStatement(sql);

            //填充占位符
            ps.setString(1,"莫扎特");
            ps.setObject(2,18);

            //执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            JDBCUtils.closeResource(connection,ps);
        }
    }

    //通用的增删改操作
    public void update(String sql,Object ...args){
        Connection connection = null;
        PreparedStatement ps = null;
        //获取数据库连接
        try {
             connection = JDBCUtils.getConnection();
             ps = connection.prepareStatement(sql);
             for(int i = 0;i<args.length;i++){
                 ps.setObject(i + 1,args[i]);
             }
             ps.execute();
             JDBCUtils.closeResource(connection,ps);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommonUpdate(){
//        String sql = "delete from customers where id = ?";
//        update(sql,3);//注意 这里是泛型数组

//        String sql1 = "Insert into user(name,address,phone)values(?,?,?)";
//        update(sql1,"金城武","Japan","12345678912");
        //order是关键字(Order By)   默认报错  必须加``
        String s1l3 = "update `order` set order_name = ? where order_id = ?";
        update(s1l3,"DD","2");
    }

}
