package com.cdsxt.service.impl;

import com.cdsxt.dao.OrderDao;
import com.cdsxt.ro.OrderInfo;
import com.cdsxt.ro.OrderProductInfo;
import com.cdsxt.ro.User;
import com.cdsxt.ro.UserAddress;
import com.cdsxt.service.OrderService;
import com.cdsxt.vo.ProductInCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private HttpServletRequest request;

    /**
     * @param productsInOrder 订单中的商品信息
     * @param userAddress     收获地址
     * @return 生成的订单的 UUID 主键
     */
    // 生成订单信息
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public String generateOrder(List<ProductInCart> productsInOrder, UserAddress userAddress, User user) {
        // 订单
        OrderInfo orderInfo = new OrderInfo();
        double sumPrice = 0;
        for (ProductInCart productInCart : productsInOrder) {
            sumPrice += productInCart.getShopPrice() * productInCart.getCount();
        }
        orderInfo.setSumPrice(sumPrice);
        orderInfo.setOrderTime(new Date());
        orderInfo.setState("未支付");
        orderInfo.setName(userAddress.getName());
        orderInfo.setPhone(userAddress.getPhone().toString());
        orderInfo.setAddr(userAddress.getAddr());
        orderInfo.setUid(user.getUid());
        orderInfo.setLogisticsComp(null);
        orderInfo.setLogisticsNum(null);
        /*Object obj = */
        this.orderDao.generateOrderInfo(orderInfo);
        // 插入语句中使用 uuid, 然后返回插入的主键值
        // 插入之后, 对象的主键属性被设置
        String oid = orderInfo.getOid();

        for (ProductInCart productInCart : productsInOrder) {
            OrderProductInfo opi = new OrderProductInfo();
            opi.setOid(oid);
            opi.setPid(productInCart.getPid());
            opi.setCount(productInCart.getCount());
            opi.setBuyPrice(productInCart.getShopPrice());
            // 执行插入
            orderDao.generateOrderProductInfo(opi);
        }
        return oid;
    }

    // 根据 id 查询订单记录
    @Override
    public OrderInfo selectOneById(String oid) {
        return orderDao.selectOneById(oid);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void updateOrderState(String oid, String state) {
        this.orderDao.updateOrderState(oid, state);
    }

    @Override
    public List<OrderInfo> selectAllOrder(Integer uid) {
        return orderDao.selectAllOrder(uid);
    }

    @Override
    public List<OrderInfo> selectOrderWithParam(Map<String, Object> params) {
        return orderDao.selectOrderWithParam(params);
    }

}
