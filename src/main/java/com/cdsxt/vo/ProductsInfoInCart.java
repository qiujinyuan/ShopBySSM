package com.cdsxt.vo;

/**
 * 购物车中的商品总量信息
 */

public class ProductsInfoInCart {

    private Integer countProCate; // 商品类别
    private Integer countPro; // 商品总数量
    private Double sumPrice; // 商品总价

    public ProductsInfoInCart() {
    }

    public ProductsInfoInCart(Integer countProCate, Integer countPro, Double sumPrice) {
        this.countProCate = countProCate;
        this.countPro = countPro;
        this.sumPrice = sumPrice;
    }

    public Integer getCountProCate() {
        return countProCate;
    }

    public void setCountProCate(Integer countProCate) {
        this.countProCate = countProCate;
    }

    public Integer getCountPro() {
        return countPro;
    }

    public void setCountPro(Integer countPro) {
        this.countPro = countPro;
    }

    public Double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Double sumPrice) {
        this.sumPrice = sumPrice;
    }
}
