package com.cdsxt.service;


import com.cdsxt.ro.OrderInfo;
import com.cdsxt.ro.User;
import com.cdsxt.ro.UserAddress;
import com.cdsxt.vo.ProductInCart;

import java.util.List;
import java.util.Map;

public interface OrderService {

    // 生成订单信息
    String generateOrder(List<ProductInCart> productsInOrder, UserAddress userAddress, User user);

    // 根据 id 查询订单记录
    OrderInfo selectOneById(String oid);

    // 更新订单状态的方法
    void updateOrderState(String oid, String state);

    // 查询所有订单信息
    List<OrderInfo> selectAllOrder(Integer uid);

    // 根据时间, 状态以及收货人查询订单
    List<OrderInfo> selectOrderWithParam(Map<String, Object> params);

}
