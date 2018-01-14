package com.cdsxt.service;


import com.cdsxt.ro.UserAddress;
import com.cdsxt.vo.ProductInCart;

import java.util.List;

public interface OrderService {

    // 生成订单信息
    void generateOrder(List<ProductInCart> productsInOrder, UserAddress userAddress);
}
