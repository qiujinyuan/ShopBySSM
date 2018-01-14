package com.cdsxt.web.cntroller;

import com.cdsxt.ro.UserFront;
import com.cdsxt.service.UserFrontService;
import com.cdsxt.util.CookieUtil;
import com.cdsxt.util.JsonUtil;
import com.cdsxt.vo.ProductInCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("shop")
public class LoginController {

    @Autowired
    private UserFrontService userFrontService;

    @RequestMapping(value = {"", "login"}, method = RequestMethod.GET)
    public String login() {
        return "front/login";
    }

    // 验证登陆
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password,
                        HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // 根据用户名查询, 如果存在则匹配密码, 正确则将当前用户存储在 session 中, 然后跳转到商城首页
        UserFront uf = this.userFrontService.selectOneByName(username);
        if (Objects.nonNull(uf)) {
            if (uf.getPassword().equals(password)) {
                // 密码正确; 将当前用户存储 session 作用域中
                request.getSession().setAttribute("curUser", uf);

                // 同时获取购物车: 其中可能包括该用户未登陆时选择的商品信息

                // Cookie 中的购物车
                String proInCart = CookieUtil.getCookieValue(request, "cart");
                // Redis 中该用户的购物车
                String proInRedis = "";
                Jedis jedis = new Jedis("localhost", 6379);
                for (Map.Entry<String, String> entry : jedis.hgetAll("cart").entrySet()) {
                    if (uf.getUsername().equals(entry.getKey())) {
                        // 当前登陆用户购物车
                        proInRedis = entry.getValue();
                    }
                }

                // 第一种情况: 服务器的 redis 数据库中包括有该用户的购物车信息, 而登陆时 Cookie 中不包括, 表示用户未进行选择商品就直接登陆
                if (StringUtils.hasText(proInRedis) && !StringUtils.hasText(proInCart)) {
                    // 将 redis 中的购物车添加到 cookie 中, 并响应出去
                    Cookie cookie = new Cookie("cart", URLEncoder.encode(proInRedis, "utf-8"));
                    // 设置路径, 所有页面都可以访问到
                    cookie.setPath("/");
                    // 设置有效期: 7 天
                    cookie.setMaxAge(60 * 60 * 24 * 7);
                    response.addCookie(cookie);
                } else if (!StringUtils.hasText(proInRedis) && StringUtils.hasText(proInCart)) {
                    // 第二种情况: 服务器中不包含该用户的购物车, 而 Cookie 中包括有, 表示用户未登陆时已经选择过商品并加入购物车
                    // 解码
                    proInCart = URLDecoder.decode(proInCart, "utf-8");
                    // 保存在 redis 中
                    jedis.hset("cart", uf.getUsername(), proInCart);

                } else if (StringUtils.hasText(proInRedis) && StringUtils.hasText(proInCart)) {
                    // 第三种情况: 服务器和 Cookie 中都包含有购物车信息, 需要合并
                    ProductInCart[] productsInCart = JsonUtil.jsonStrToArr(URLDecoder.decode(proInCart, "utf-8"), ProductInCart.class);
                    ProductInCart[] productsInRedis = JsonUtil.jsonStrToArr(proInRedis, ProductInCart.class);

                    List<ProductInCart> products = new ArrayList<>();
                    if (Objects.nonNull(productsInCart) && Objects.nonNull(productsInRedis)) {
                        // 添加到列表中
                        products.addAll(Arrays.asList(productsInCart));
                        products.addAll(Arrays.asList(productsInRedis));
                    }
                    if (products.size() > 0) {
                        // 如果商品相同, 则相加数量
                        // 升序排列
                        products.sort((o1, o2) -> {
                            if (o1.getPid() > o2.getPid()) {
                                return 1;
                            } else if (o1.getPid() < o2.getPid()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        });
                        // 比较相邻的两个元素, 保存商品数量多的一方, 并删除第二个元素;
                        for (int i = 0; i < products.size(); i++) {
                            ProductInCart pic1 = products.get(i);
                            if (i != products.size() - 1) {
                                // 不是最后一个元素
                                ProductInCart pic2 = products.get(i + 1);
                                if (pic1.getPid().equals(pic2.getPid())) {
                                    // 比较商品数量: 保存多的一方
                                    if (pic1.getCount() < pic2.getCount()) {
                                        pic1.setCount(pic2.getCount());
                                    }
                                    // 删除第二个
                                    products.remove(i + 1);
                                }
                            }
                        }
                        // 更新 Cookie 和 redis 中的信息, 保持同步
                        String cartString = JsonUtil.objToJsonStr(products);

                        if (Objects.nonNull(cartString)) {
                            jedis.hset("cart", uf.getUsername(), cartString);

                            Cookie cookie = new Cookie("cart", URLEncoder.encode(cartString, "utf-8"));
                            cookie.setPath("/");
                            // 设置有效期: 7 天
                            cookie.setMaxAge(60 * 60 * 24 * 7);
                            response.addCookie(cookie);
                        }
                    }
                }

                // 请求转发
                return "forward:/products/index";
            }
        }
        return "front/login";
    }



    // 退出登录
    @RequestMapping(value = "loginOut", method = RequestMethod.GET)
    public String loginOut(HttpServletRequest request) {
        UserFront curUser = (UserFront) request.getSession().getAttribute("curUser");
        if (Objects.nonNull(curUser)) {
            // 使 session 失效
            request.getSession().invalidate();
        }
        return "redirect:/shop";
    }

    // 进入商城首页
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "front/index";
    }
}
