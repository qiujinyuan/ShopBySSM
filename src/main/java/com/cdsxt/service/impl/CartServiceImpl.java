package com.cdsxt.service.impl;

import com.cdsxt.redis.RedisDao;
import com.cdsxt.service.CartService;
import com.cdsxt.vo.ProductInCart;
import com.cdsxt.vo.ProductsInfoInCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisDao redisDao;

    // 计算购物车中的商品总量信息
    @Override
    public ProductsInfoInCart countProductInCart(ProductInCart[] productsInCart) {
        ProductsInfoInCart piic = new ProductsInfoInCart();
        piic.setCountProCate(productsInCart.length); // 商品数量
        Integer countPro = 0; // 商品总数量
        Double sumPrice = 0.0; // 商品总价
        for (int i = 0; i < productsInCart.length; i++) {
            countPro += productsInCart[i].getCount();
            sumPrice += productsInCart[i].getShopPrice() * productsInCart[i].getCount();
        }
        piic.setCountPro(countPro);
        piic.setSumPrice(sumPrice);
        return piic;
    }


    /**
     * 返回指定用户保存在 redis 中的购物车字符串
     *
     * @param username 指定用户
     * @return 购物车 json 字符串
     */
    @Override
    public String getCartStrFromRedis(String username) {
        return redisDao.getByKeyInCart(username);
    }

    @Override
    public void setCartStrToRedis(String username, String cartStr) {
        redisDao.setCartStrToRedis(username, cartStr);
    }


}
