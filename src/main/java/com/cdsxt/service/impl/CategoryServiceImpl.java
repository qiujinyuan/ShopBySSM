package com.cdsxt.service.impl;

import com.cdsxt.dao.CategoryDao;
import com.cdsxt.ro.Category;
import com.cdsxt.ro.CategorySecond;
import com.cdsxt.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    // 查询所有一级分类
    @Override
    public List<Category> selectAllCategory() {
        return categoryDao.selectAllCategory();
    }

    // 查询所有二级分类
    @Override
    public List<CategorySecond> selectAllCategorySecond() {
        return categoryDao.selectAllCategorySecond();
    }
}
