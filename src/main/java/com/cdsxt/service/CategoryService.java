package com.cdsxt.service;

import com.cdsxt.ro.Category;
import com.cdsxt.ro.CategorySecond;

import java.util.List;

public interface CategoryService {

    List<Category> selectAllCategory();

    List<CategorySecond> selectAllCategorySecond();
}
