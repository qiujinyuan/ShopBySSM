package com.cdsxt.web.cntroller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.cdsxt.config.AlipayConfig;
import com.cdsxt.ro.OrderInfo;
import com.cdsxt.ro.UserFront;
import com.cdsxt.service.CartService;
import com.cdsxt.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

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
    public String pay(String oid) throws AlipayApiException, UnsupportedEncodingException {

        // 查询出订单信息
        OrderInfo orderInfo = this.orderService.selectOneById(oid);

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String(oid.getBytes("ISO-8859-1"), "UTF-8");
        //付款金额，必填
        String total_amount = new String((orderInfo.getSumPrice() + "").getBytes("ISO-8859-1"), "UTF-8");
        //订单名称，必填
        String subject = new String(("收货人: " + orderInfo.getName() + ", 下单日期: " + orderInfo.getOrderTime()).getBytes("ISO-8859-1"), "UTF-8");
        //商品描述，可空
        String body = new String(("收获地址:" + orderInfo.getAddr()).getBytes("ISO-8859-1"), "UTF-8");

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
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
     * 同步
     *
     * @param outTradeNo 商户订单号, 即 order_info的主键
     * @return 到购物车页面
     */
    @RequestMapping(value = "returnUrl", method = RequestMethod.GET)
    public String returnUrl(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {

        boolean signVerified = this.verifySignature(request);

        //——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            String orderInfo = "交易号:" + trade_no + "<br/>订单号:" + out_trade_no + "<br/>付款金额:" + total_amount;

            // 1. 存储订单详细信息
            model.addAttribute("orderInfo", orderInfo);

            // 2. 更新 cookie 中的数据
            // 查询出 redis 中支付之前保存的购物车信息
            UserFront uf = (UserFront) request.getSession().getAttribute("curUser");
            this.cartController.setCartToCookie(this.cartService.getCartStrFromRedis(uf.getUsername()), response);
            // 返回到支付成功同步通知页面
            return "pay/return_url";

        } else {
            model.addAttribute("tip", "验签失败");
            return "pay/return_url";
        }
    }


    /**
     * 异步
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws AlipayApiException
     */
    @RequestMapping(value = "notifyUrl", method = RequestMethod.POST)
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {

        boolean signVerified = this.verifySignature(request);

        PrintWriter pw = response.getWriter();
        response.setHeader("content-type", "text/html;charset=utf-8");
        //——请在这里编写您的程序（以下代码仅作参考）——

	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        if (signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                System.out.println("[商户订单号]:" + out_trade_no);
                // 根据 订单号进行查找
                OrderInfo orderAfterPay = this.orderService.selectOneById(out_trade_no);
                if (Objects.nonNull(orderAfterPay) && orderAfterPay.getSumPrice().equals(Double.parseDouble(total_amount))) {
                    // 存在订单
                    // 1. 修改订单状态为 "已付款"
                    this.orderService.updateOrderState(out_trade_no, "已付款");
                }
            }
            pw.write("success");
        } else {//验证失败
            pw.write("fail");

            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }

        //——请在这里编写您的程序（以上代码仅作参考）——

    }

    // 验签
    private boolean verifySignature(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {

        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        System.out.println("报文: " + params);

        return AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
    }

}
