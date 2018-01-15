package com.cdsxt.service;


import com.cdsxt.ro.OrderInfo;
import com.cdsxt.ro.UserAddress;
import com.cdsxt.ro.UserFront;
import com.cdsxt.vo.ProductInCart;

import java.util.List;

public interface OrderService {

    // 生成订单信息
    String generateOrder(List<ProductInCart> productsInOrder, UserAddress userAddress, UserFront userFront);

    // 根据 id 查询订单记录
    OrderInfo selectOneById(String oid);

    // 更新订单状态的方法
    void updateOrderState(String oid, String state);

    // 查询所有订单信息
    List<OrderInfo> selectAllOrder();
}
