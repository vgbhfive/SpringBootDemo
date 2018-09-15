package com.vgbh.mybatisdemo.dao;

import com.vgbh.mybatisdemo.entity.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * @time:
 * @author: Vgbh
 */
public class TestStudentDao {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void init() {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis_config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectAll() {
        SqlSession sqlSession = null;

        try {
            sqlSession = sqlSessionFactory.openSession();
            List<Student> list = sqlSession.selectList("selectAll");// 找不到配置文件从而导致的错误，并且数据库没有创建表和添加数据

            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

}
