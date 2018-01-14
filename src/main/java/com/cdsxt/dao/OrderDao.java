package com.cdsxt.dao;

import com.cdsxt.ro.OrderInfo;
import com.cdsxt.ro.OrderProductInfo;

public interface OrderDao {

    void generateOrderInfo(OrderInfo orderInfo);

    void generateOrderProductInfo(OrderProductInfo orderProductInfo);
}
