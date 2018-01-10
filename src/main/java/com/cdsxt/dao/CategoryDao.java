package com.cdsxt.dao;

import com.cdsxt.ro.Category;
import com.cdsxt.ro.CategorySecond;

import java.util.List;

public interface CategoryDao {

    List<Category> selectAllCategory();

    List<CategorySecond> selectAllCategorySecond();
}
