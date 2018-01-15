package com.cdsxt.web.cntroller;

import com.alipay.api.AlipayApiException;
import com.cdsxt.ro.OrderInfo;
import com.cdsxt.ro.UserAddress;
import com.cdsxt.ro.UserFront;
import com.cdsxt.service.CartService;
import com.cdsxt.service.OrderService;
import com.cdsxt.service.UserFrontService;
import com.cdsxt.util.JsonUtil;
import com.cdsxt.vo.ProductInCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequestMapping("order")
@Controller
public class OrderController {


    @Autowired
    private CartService cartService;

    @Autowired
    private UserFrontService userFrontService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private Alipay alipay;

    /**
     * 返回订单页面
     *
     * @param ids 需要结算的商品
     * @return 到订单页面
     */
    @RequestMapping(value = "checkIn", method = RequestMethod.GET)
    public String checkIn(Integer[] ids, HttpServletRequest request, Model model) {
        // 获取当前用户
        UserFront uf = (UserFront) request.getSession().getAttribute("curUser");

        String productInCart = this.cartService.getCartStrFromRedis(uf.getUsername());

        // 解析购物车字符串, 与订单中的商品 id 进行比较, 相同则表示该商品需要结算, 就返回到页面上
        List<ProductInCart> productsInOrders = new ArrayList<>();
        // 计算总价
        double sumPrice = 0;
        if (StringUtils.hasText(productInCart)) {
            List<ProductInCart> productsInOrder = Arrays.asList(JsonUtil.jsonStrToArr(productInCart, ProductInCart.class));
            // 对订单数组进行排序
            Arrays.sort(ids);
            for (int i = 0; i < productsInOrder.size(); i++) {
                ProductInCart pic = productsInOrder.get(i);
                if (Arrays.binarySearch(ids, pic.getPid()) >= 0) {
                    // 找到, 表示该商品在订单中
                    productsInOrders.add(pic);
                    sumPrice += pic.getShopPrice() * pic.getCount();
                }
            }
            model.addAttribute("productsInOrder", productsInOrders);
            model.addAttribute("sumPrice", sumPrice);
        }
        // 存入用户的收获地址信息
        model.addAttribute("userAddresses", this.userFrontService.selectUserAddressById(uf.getUid()));
        // 将需要结算的商品 id 返回, 以便下次获取
        model.addAttribute("ids", Arrays.toString(ids).substring(1, Arrays.toString(ids).length() - 1));
        return "front/order";
    }


    /**
     * 生成订单
     *
     * @param ids       已下单的商品
     * @param address   收获地址 id
     * @param payMethod 付款方式
     * @return
     */
    // 获取订单信息, 并存入到 order_info 表中
    @RequestMapping(value = "checkIn", method = RequestMethod.POST)
    public void checkIn(Integer[] ids, Integer address, String payMethod, HttpServletRequest request,
                        HttpServletResponse response) throws IOException, AlipayApiException {
        // 获取当前用户
        UserFront uf = (UserFront) request.getSession().getAttribute("curUser");

        String productInCart = this.cartService.getCartStrFromRedis(uf.getUsername());

        // 要支付的订单信息
        List<ProductInCart> productsInOrders = new ArrayList<>();
        // 购物车中剩余商品
        List<ProductInCart> productsInCartLeave = new ArrayList<>();
        if (StringUtils.hasText(productInCart)) {
            List<ProductInCart> productsInOrder = Arrays.asList(JsonUtil.jsonStrToArr(productInCart, ProductInCart.class));
            Arrays.sort(ids);
            for (int i = 0; i < productsInOrder.size(); i++) {
                ProductInCart pic = productsInOrder.get(i);
                if (Arrays.binarySearch(ids, pic.getPid()) >= 0) {
                    productsInOrders.add(pic);
                } else {
                    // 订单以外的商品加入到剩余商品列表中
                    productsInCartLeave.add(pic);
                }
            }
        }
        UserAddress userAddress = this.userFrontService.selectAddressById(address);
        // 生成订单数据, 保存在数据库中, 返回主键
        String oid = this.orderService.generateOrder(productsInOrders, userAddress, uf);

        // 1. 需要在提交订单后, 同步修改 redis 和 cookie 中的数据, 此处先修改 redis 中的数据;
        // 待支付完成后跳转到 returnUrl() 中再更新 cookie, 以便响应 cookie
        String cartString = JsonUtil.objToJsonStr(productsInCartLeave);
        this.cartService.setCartStrToRedis(uf.getUsername(), cartString);

        // 根据 payMethod 调用不同的支付方法
        PrintWriter pw = response.getWriter();
        response.setHeader("content-type", "text/html;charset=utf-8");
        if ("alipay".equals(payMethod)) {
            // 调用支付宝付款
            // 写出页面, 进行付款
            pw.write(alipay.pay(oid));
        } else if ("wechat".equals(payMethod)) {
            pw.write("<h2>微信付款</h2>");
        }
    }

    // 查询所有订单信息, 返回到 "我的订单" 中显示
    @RequestMapping(value = "selectAllOrder", method = RequestMethod.GET)
    public String selectAllOrder(Model model) {
        List<OrderInfo> allOrders = this.orderService.selectAllOrder();
        model.addAttribute("allOrders", allOrders);
        return "front/allOrder";
    }


}
