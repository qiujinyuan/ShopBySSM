package com.cdsxt.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MybatisUtil {

    private static SqlSessionFactory factory;
//	private static int a=111;
//	private  int b=22;

    static {
        try {
            //配置文件的路径
            String resource = "mybatis-config.xml";
            //文件的输入流
            //MybatisUtil.class.getResourceAsStream(name)
            InputStream inputStream = Resources.getResourceAsStream(resource);
            //sqlsession工厂的创建器
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

            //sqlsession工厂
            factory = builder.build(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取sqlsesison-简单的session-每次执行sql都会马上发送sql
    public static SqlSession getSession() {
        SqlSession sqlSession = factory.openSession();
        return sqlSession;
    }
    //获取sqlsesison-批量执行的session

    /**
     * 该session可以多次执行，发送一次sql-用于批量执行sql
     * 发送sql-手动调用：flushStatements即可
     */
    public static SqlSession getSessionBatch() {
        SqlSession sqlSession = factory.openSession(ExecutorType.BATCH);
        return sqlSession;
    }
}
