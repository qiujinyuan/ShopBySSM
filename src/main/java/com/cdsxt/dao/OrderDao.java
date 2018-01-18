package com.cdsxt.dao;

import com.cdsxt.ro.OrderInfo;
import com.cdsxt.ro.OrderProductInfo;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    void generateOrderInfo(OrderInfo orderInfo);

    void generateOrderProductInfo(OrderProductInfo orderProductInfo);

    // 根据 id 查询订单记录
    OrderInfo selectOneById(String oid);

    // 更新订单状态的方法
    void updateOrderState(String oid, String state);

    // 查询所有订单信息
    List<OrderInfo> selectAllOrder(Integer uid);

    // 根据时间, 状态以及收货人查询订单
    List<OrderInfo> selectOrderWithParam(Map<String, Object> params);
}
