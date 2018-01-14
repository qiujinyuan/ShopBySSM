package com.cdsxt.web.cntroller;

// 购物车

import com.cdsxt.ro.UserFront;
import com.cdsxt.service.CartService;
import com.cdsxt.util.CookieUtil;
import com.cdsxt.util.JsonUtil;
import com.cdsxt.vo.ProductInCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Objects;

@RequestMapping("carts")
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "cart")
    public String index(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        // 购物车中的商品
        String proInCart = CookieUtil.getCookieValue(request, "cart");

        // 购物车为空, 则不进行处理
        // 购物车如果不存在, 则不会传输过来任何值
        if (StringUtils.hasText(proInCart)) {
            // 解码
            proInCart = URLDecoder.decode(proInCart, "utf-8");
            UserFront uf = (UserFront) request.getSession().getAttribute("curUser");
            if (Objects.nonNull(uf)) {
                // 如果当前存在登陆用户: 更新 redis 中的数据
                Jedis jedis = new Jedis("localhost", 6379);
                jedis.hset("cart", uf.getUsername(), proInCart);
            }
            ProductInCart[] productsInCart = JsonUtil.jsonStrToArr(proInCart, ProductInCart.class);
            if (Objects.nonNull(productsInCart) && productsInCart.length > 0) {
                // 存储作用域
                model.addAttribute("productsInCart", productsInCart);
                // 计算购物车中的商品总量信息
                model.addAttribute("productsInfoInCart", this.cartService.countProductInCart(productsInCart));
            }
        }
        return "front/cart";
    }
}
