package com.cdsxt.service;

import com.cdsxt.vo.ProductInCart;
import com.cdsxt.vo.ProductsInfoInCart;

public interface CartService {

    // 计算购物车中的商品总量信息
    ProductsInfoInCart countProductInCart(ProductInCart[] productsInCart);
}
