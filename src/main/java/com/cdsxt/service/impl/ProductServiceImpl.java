package com.cdsxt.service.impl;

import com.cdsxt.dao.ProductDao;
import com.cdsxt.ro.Product;
import com.cdsxt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> selectAllHotProducts() {
        return productDao.selectAllHotProducts();
    }

    @Override
    public List<Product> selectAllNewProducts() {
        return productDao.selectAllNewProducts();
    }

    @Override
    public Product selectById(Integer pid) {
        return productDao.selectById(pid);
    }

    @Override
    public List<Product> selectProductWithParam(String productName, Double minPrice, Double maxPrice, String categoryName) {
        Map<String, Object> params = new HashMap<>();
        params.put("productName", productName);
        params.put("minPrice", minPrice);
        params.put("maxPrice", maxPrice);
        params.put("categoryName", categoryName);
        return this.productDao.selectProductWithParam(params);
    }

    @Override
    public List<Product> selectProductWithCategorySecond(Integer csid) {
        return this.productDao.selectProductWithCategorySecond(csid);
    }

    @Override
    public List<Product> selectProductWithCategory(Integer cid) {
        return this.productDao.selectProductWithCategory(cid);
    }
}
