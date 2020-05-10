package com.atguigu.preparedstatement3.crud;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/*
使用Preparedstatement来替换Statement , 实现对数据表的增删改操作
 */
public class PreparedStatementUpdateTest {
     //向customers表中添加一条记录
    @Test
    public void testInsert() throws IOException, ClassNotFoundException, SQLException {
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
        //?是占位符
        //预编译sql语句，返回PreparedStatement实例
        String sql = "Insert into customers(name,email,birth)values(?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        //填充占位符
        //注意：这里的parameterIndex要从1开始  因为数据库的index是从1开始的 不要和数组弄混了
        //name的类型是String birth在数据库的类型是date  在java里面对应sql.date
         ps.setString(1,"金城武");
    }
}
