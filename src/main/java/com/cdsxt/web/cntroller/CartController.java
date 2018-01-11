package com.cdsxt.web.cntroller;

// 购物车

import com.cdsxt.vo.ProductInCart;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequestMapping("carts")
@Controller
public class CartController {

    @RequestMapping(value = "cart", method = RequestMethod.GET)
    public String index(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        // 购物车中的商品
        String proInCart = "";
        for (Cookie cookie : cookies) {
            if ("cart".equals(cookie.getName())) {
                // 获取 cart cookie
                proInCart = URLDecoder.decode(cookie.getValue(), "UTF-8");
            }
        }
        // 购物车为空, 则不进行处理
        if (Objects.nonNull(proInCart) && StringUtils.hasText(proInCart)) {
            proInCart = proInCart.substring(1, proInCart.length() - 1);
            String[] jsons = proInCart.split("},");
            List<ProductInCart> productInCarts = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < jsons.length; i++) {
                if (i + 1 == jsons.length) {
                    productInCarts.add(gson.fromJson(jsons[i], ProductInCart.class));
                } else {
                    // 解析json 字符串, 并映射成对象
                    productInCarts.add(gson.fromJson(jsons[i] + "}", ProductInCart.class));
                }
            }
            model.addAttribute("productInCarts", productInCarts);
        }

        return "front/cart";
    }
}
