package com.cdsxt.web.cntroller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.cdsxt.config.AlipayConfig;
import com.cdsxt.ro.OrderInfo;
import com.cdsxt.ro.UserFront;
import com.cdsxt.service.CartService;
import com.cdsxt.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于构建支付页面, 并完成付款
 */

@Controller
@RequestMapping("alipay")
public class Alipay {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartController cartController;

    @Autowired
    private CartService cartService;

    /**
     * 调用支付宝付款
     *
     * @param oid 订单的 id
     * @return result 用于输出页面
     */
    @RequestMapping(value = "pay", method = RequestMethod.GET)
    public String pay(String oid) throws AlipayApiException {

        // 查询出订单信息
        //商户订单号，商户网站订单系统中唯一订单号，必填
        OrderInfo orderInfo = this.orderService.selectOneById(oid);

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //付款金额，必填
        String total_amount = orderInfo.getSumPrice() + "";
        //订单名称，必填
        String subject = "收货人: " + orderInfo.getName() + ", 下单日期: " + orderInfo.getOrderTime();
        //商品描述，可空
        String body = "收获地址:" + orderInfo.getAddr();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + oid + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        return alipayClient.pageExecute(alipayRequest).getBody();
    }

    /**
     * 付款成功后进入的跳转页面的方法
     *
     * @param out_trade_no 商户订单号, 即 order_info的主键
     * @return 到购物车页面
     */
    @RequestMapping(value = "returnUrl", method = RequestMethod.GET)
    public String returnUrl(@RequestParam("out_trade_no") String outTradeNo, HttpServletRequest request, HttpServletResponse response) {
        // 1. 修改订单状态为 "已付款"
        this.orderService.updateOrderState(outTradeNo, "已付款");

        // 2. 更新 cookie 中的数据
        // 查询出 redis 中支付之前保存的购物车信息
        UserFront uf = (UserFront) request.getSession().getAttribute("curUser");
        this.cartController.setCartToCookie(this.cartService.getCartStrFromRedis(uf.getUsername()), response);
        // 返回到商城首页
        return "forward:/products/index";
    }


    @RequestMapping(value = "notifyUrl", method = RequestMethod.GET)
    public String notifyUrl() {
        return "pay/notify_url";
    }
}
