package com.cdsxt.service;

import com.cdsxt.ro.Product;

import java.util.List;

public interface ProductService {

    List<Product> selectAllHotProducts();

    List<Product> selectAllNewProducts();

    Product selectById(Integer pid);

    List<Product> selectProductWithParam(String productName, Double minPrice, Double maxPrice, String categoryName);

    List<Product> selectProductWithCategorySecond(Integer csid);

    List<Product> selectProductWithCategory(Integer cid);
}
