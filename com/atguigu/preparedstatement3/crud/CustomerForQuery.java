package com.atguigu.preparedstatement3.crud;

import com.atguigu.bean.Customer;
import com.atguigu.util3.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

//针对Customer表的各个字段操作
public class CustomerForQuery {
    @Test
    public void testQuery1() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1,1);
            //执行 返回结果集
            resultSet = ps.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //将数据封装为一个对象
                Customer customer = new Customer(id,name,email,birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,ps,resultSet);
        }
    }
     //针对于customer表的通用查询操作
    public Customer queryForCustomers(String sql,Object ...args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);
            for(int i = 0;i<args.length;i++){
                ps.setObject(i + 1,args[i]);
            }
            resultSet = ps.executeQuery();
            //args的长度 和  结果集要返回的列  没有关系

            ResultSetMetaData metaData = resultSet.getMetaData();
            int count = metaData.getColumnCount();
            if(resultSet.next()){
                Customer customer = new Customer();
                for(int i = 0;i<count;i++){
                    Object columnvalue = resultSet.getObject(i + 1);

                    //获取每个列的列名
                    String columnName = metaData.getColumnName(i + 1);
                    //将customer对象的  columnName属性 赋值为value
                    //反射：对运行时类的指定属性的 赋值操作
                    //这里 一定要保证 ORM 对象和表字段的名称相同
                    Field field = Customer.class.getDeclaredField(columnName);
                    //这个属性有可能私有
                    field.setAccessible(true);
                    //这里要指定 是哪个对象  因为它已经确定是哪个类的属性了 但我们还要具体到某个具体对象
                    field.set(customer,columnvalue);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,ps,resultSet);
        }
        return null;
    }
    @Test
    public void testQueryForCustomers(){
        //这里顺序随意
        String sql = "select name,id,birth,email from customers where id = ?";
        Customer customer = queryForCustomers(sql, 13);
        System.out.println(customer);

        sql = "select email from customers where name = ?";
        Customer customer1 = queryForCustomers(sql, "周杰伦");
        System.out.println(customer1);
    }
}
