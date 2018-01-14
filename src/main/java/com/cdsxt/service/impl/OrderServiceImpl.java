package com.cdsxt.service.impl;

import com.cdsxt.dao.OrderDao;
import com.cdsxt.ro.OrderInfo;
import com.cdsxt.ro.OrderProductInfo;
import com.cdsxt.ro.UserAddress;
import com.cdsxt.ro.UserFront;
import com.cdsxt.service.OrderService;
import com.cdsxt.vo.ProductInCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private HttpServletRequest request;

    // 生成订单信息
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void generateOrder(List<ProductInCart> productsInOrder, UserAddress userAddress) {
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
        orderInfo.setUid(((UserFront) request.getSession().getAttribute("curUser")).getUid());
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
    }
}
