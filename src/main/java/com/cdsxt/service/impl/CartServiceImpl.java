package com.cdsxt.service.impl;

import com.cdsxt.service.CartService;
import com.cdsxt.vo.ProductInCart;
import com.cdsxt.vo.ProductsInfoInCart;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    // 计算购物车中的商品总量信息
    @Override
    public ProductsInfoInCart countProductInCart(ProductInCart[] productsInCart) {
        ProductsInfoInCart piic = new ProductsInfoInCart();
        piic.setCountProCate(productsInCart.length); // 商品数量
        Integer countPro = 0; // 商品总数量
        Double sumPrice = 0.0; // 商品总价
        for (int i = 0; i < productsInCart.length; i++) {
            countPro += productsInCart[i].getCount();
            sumPrice += productsInCart[i].getShopPrice() * productsInCart[i].getCount();
        }
        piic.setCountPro(countPro);
        piic.setSumPrice(sumPrice);
        return piic;
    }



}
