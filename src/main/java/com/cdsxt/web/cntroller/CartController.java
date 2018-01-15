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
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
                this.cartService.setCartStrToRedis(uf.getUsername(), proInCart);
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


    // 将 cookie 响应给客户端
    public void setCartToCookie(String cookieStr, HttpServletResponse response) {
        if (Objects.nonNull(cookieStr)) {
            Cookie cookie = null;
            try {
                cookie = new Cookie("cart", URLEncoder.encode(cookieStr, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置有效期: 7 天
            Objects.requireNonNull(cookie).setMaxAge(60 * 60 * 24 * 7);
            // 设置路径, 所有页面都可以访问到
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }
}
