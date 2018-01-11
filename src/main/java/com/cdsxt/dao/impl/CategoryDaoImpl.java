package com.cdsxt.dao.impl;

import com.cdsxt.dao.CategoryDao;
import com.cdsxt.ro.Category;
import com.cdsxt.ro.CategorySecond;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }

    // 查询所有一级分类
    @Override
    public List<Category> selectAllCategory() {
        System.out.println(this.getSession().getClass());
        return this.getSession().selectList("category.selectAllCategory");
    }

    // 查询所有二级分类: 二级分类中包含有一级分类
    @Override
    public List<CategorySecond> selectAllCategorySecond() {
        return this.getSession().selectList("category.selectAllCategorySecond");
    }
}
