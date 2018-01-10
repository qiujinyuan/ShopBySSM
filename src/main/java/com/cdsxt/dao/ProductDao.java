package com.cdsxt.dao;

import com.cdsxt.ro.Product;

import java.util.List;
import java.util.Map;

public interface ProductDao {

    List<Product> selectAll();

    List<Product> selectAllHotProducts();

    List<Product> selectAllNewProducts();

    Product selectById(Integer pid);

    List<Product> selectProductWithParam(Map<String, Object> params);

    List<Product> selectProductWithCategorySecond(Integer csid);

    List<Product> selectProductWithCategory(Integer cid);
}
