package com.cdsxt.web.cntroller;

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
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@RequestMapping("order")
@Controller
public class OrderController {


    @Autowired
    private CartService cartService;

    @Autowired
    private UserFrontService userFrontService;

    @Autowired
    private OrderService orderService;

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

        String productInCart = "";
        // 根据当前用户, 查询 redis 中保存的购物车中商品信息
        Jedis jedis = new Jedis();
        for (Map.Entry<String, String> entry : jedis.hgetAll("cart").entrySet()) {
            if (uf.getUsername().equals(entry.getKey())) {
                productInCart = entry.getValue();
            }
        }
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
    public String checkIn(Integer[] ids, Integer address, String payMethod, HttpServletRequest request,
                          HttpServletResponse response, Model model) throws UnsupportedEncodingException {
        UserFront uf = (UserFront) request.getSession().getAttribute("curUser");

        String productInCart = "";
        Jedis jedis = new Jedis();
        for (Map.Entry<String, String> entry : jedis.hgetAll("cart").entrySet()) {
            if (uf.getUsername().equals(entry.getKey())) {
                productInCart = entry.getValue();
            }
        }
        // 要支付的订单信息
        List<ProductInCart> productsInOrders = new ArrayList<>();
        // 购物车中剩下的商品
        List<ProductInCart> productsInCartLeave = new ArrayList<>();
        if (StringUtils.hasText(productInCart)) {
            List<ProductInCart> productsInOrder = Arrays.asList(JsonUtil.jsonStrToArr(productInCart, ProductInCart.class));
            Arrays.sort(ids);
            for (int i = 0; i < productsInOrder.size(); i++) {
                ProductInCart pic = productsInOrder.get(i);
                if (Arrays.binarySearch(ids, pic.getPid()) >= 0) {
                    productsInOrders.add(pic);
                } else {
                    // 订单以外的商品加入到剩下商品列表中
                    productsInCartLeave.add(pic);
                }
            }
        }
        UserAddress userAddress = this.userFrontService.selectAddressById(address);
        this.orderService.generateOrder(productsInOrders, userAddress);

        // 根据 payMethod 返回不同的支付页面


        // 更新 cookie 和 redis 中的商品信息: 除去订单中的商品
        String cartString = JsonUtil.objToJsonStr(productsInCartLeave);

        if (Objects.nonNull(cartString)) {
            // 更新剩下的
            jedis.hset("cart", uf.getUsername(), cartString);
            Cookie cookie = new Cookie("cart", URLEncoder.encode(cartString, "utf-8"));
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        // 不能转发, 转发到 cart 后会重新获取 cookie, 并设置
        // 应该直接返回
        model.addAttribute("productsInCart", productsInCartLeave);
        // 计算购物车中的商品总量信息: 页面上会进行计算, 此处可以省略
        // model.addAttribute("productsInfoInCart", this.cartService.countProductInCart((ProductInCart[]) productsInCartLeave.toArray()));

        return "front/cart";
    }

}
