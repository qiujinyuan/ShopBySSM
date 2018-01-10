package com.cdsxt.dao.impl;

import com.cdsxt.dao.CategoryDao;
import com.cdsxt.ro.Category;
import com.cdsxt.ro.CategorySecond;
import com.cdsxt.util.MybatisUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    // 查询所有一级分类
    @Override
    public List<Category> selectAllCategory() {
        return MybatisUtil.getSession().selectList("category.selectAllCategory");
    }

    // 查询所有二级分类: 二级分类中包含有一级分类
    @Override
    public List<CategorySecond> selectAllCategorySecond() {
        return MybatisUtil.getSession().selectList("category.selectAllCategorySecond");
    }
}
