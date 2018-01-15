package com.cdsxt.dao;

import com.cdsxt.ro.OrderInfo;
import com.cdsxt.ro.OrderProductInfo;

import java.util.List;

public interface OrderDao {

    void generateOrderInfo(OrderInfo orderInfo);

    void generateOrderProductInfo(OrderProductInfo orderProductInfo);

    // 根据 id 查询订单记录
    OrderInfo selectOneById(String oid);

    // 更新订单状态的方法
    void updateOrderState(String oid, String state);

    // 查询所有订单信息
    List<OrderInfo> selectAllOrder();
}
