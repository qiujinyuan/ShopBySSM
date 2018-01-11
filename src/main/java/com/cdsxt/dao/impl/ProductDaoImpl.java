package com.cdsxt.dao.impl;

import com.cdsxt.dao.ProductDao;
import com.cdsxt.ro.Product;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }


    // 查询所有商品
    @Override
    public List<Product> selectAll() {
        return this.getSession().selectList("product.selectAll");
    }

    // 查询所有最热商品
    @Override
    public List<Product> selectAllHotProducts() {
        return this.getSession().selectList("product.selectAllHotProducts");
    }

    // 查询最近三个月以内的所有商品
    @Override
    public List<Product> selectAllNewProducts() {
        return this.getSession().selectList("product.selectAllNewProducts");
    }

    // 根据 id 查询 product
    @Override
    public Product selectById(Integer pid) {
        return this.getSession().selectOne("product.selectById", pid);
    }

    // 有条件查询
    @Override
    public List<Product> selectProductWithParam(Map<String, Object> params) {
        // String productName, Long minPrice, Long maxPrice, String categoryName
        SqlSession sqlSession = this.getSession();
        return sqlSession.selectList("product.selectProductWithParam", params);
    }

    // 根据商品种类 id 进行查询
    @Override
    public List<Product> selectProductWithCategorySecond(Integer csid) {
        SqlSession sqlSession = this.getSession();
        return sqlSession.selectList("product.selectProductWithCategorySecond", csid);
    }

    @Override
    public List<Product> selectProductWithCategory(Integer cid) {
        SqlSession sqlSession = this.getSession();
        return sqlSession.selectList("product.selectProductWithCategory", cid);
    }

}
