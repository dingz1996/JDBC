package com.atguigu.connectin1;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    //方式一
    @Test
    public void testConnection1() throws SQLException {
        //这里出现了第三方的API
        //获取driver的实现类对象
        Driver driver = new com.mysql.jdbc.Driver();

        //将用户名和密码封装在properties中
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");
        //提供要连接的数据库
        String url = "jdbc:mysql://121.42.231.230:3306/test";

        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }
    //方式二：对方式一的迭代:在如下的程序中不出现第三方的api，使得程序具有更好的可移植性
    @Test
    public void testConnection2() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //获取driver的实现类对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver)clazz.newInstance();//这里能保证newInstance：必须有空参构造器，且是public
        //将用户名和密码封装在properties中
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");
        //提供要连接的数据库
        String url = "jdbc:mysql://121.42.231.230:3306/test";

        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }
    //方式三：使用DriverManager替换Driver
    @Test
    public void testConnection3() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //提供要连接的数据库
        String url = "jdbc:mysql://121.42.231.230:3306/test";
        String user = "root";
        String password = "123456";

        //获取driver的实现类对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver)clazz.newInstance();//这里能保证newInstance：必须有空参构造器，且是public
        //注册Driver类
        //注意：DriverManager是java.sql下的  这里使用的都是static方法
        DriverManager.registerDriver(driver);

        Connection connect = DriverManager.getConnection(url,user,password);
        System.out.println(connect);
    }

    //方式四：可以只是加载驱动，不用显示的注册驱动。
    @Test
    public void testConnection4() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        // 1.提供三个连接的基本信息:
        String url = "jdbc:mysql://121.42.231.230:3306/test";
        String user = "root";
        String password = "123456";
        // 2.加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //相较于方式三 省略如下操作
//        Driver driver = (Driver)clazz.newInstance();
//         //注册驱动
//        DriverManager.registerDriver(driver);

        /*
        原因如下： static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
         */
        // 3.获取连接
        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }
  //最终版：将数据库连接需要的4个基本信息声明在配置文件中，通过读取配置文件的方式，获取连接
    /*
    此种方式的好处？
    1.实现了数据与代码的分离，实现了解耦。
    2.将工程打成jar包发布后，如果需要修改配置文件信息，可直接替换文件，避免了程序的重新编译。
     */
    @Test
    public void testConnection5() throws IOException, ClassNotFoundException, SQLException {
       //1.读取配置文件中的4个基本信息  这里的类加载器是Application ClassLoader
        //默认识别路径是src下
        //生成一个IO流
        InputStream is =  ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
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
        System.out.println(connection);
    }

}
