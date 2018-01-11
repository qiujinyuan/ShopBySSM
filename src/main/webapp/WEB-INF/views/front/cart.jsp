<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>购物车</title>
    <%@ include file="common-base.jsp" %>
    <link href="assets/css/cart.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<%@ include file="common-header.jsp" %>
<div class="container cart">
    <div class="span24">
        <div class="step step1" style="color: green;font-size: 20pt;font-weight: bold;">
            购物车信息
        </div>
        <div class="step step1" style="color: red;font-size: 20pt;font-weight: bold;">
            (提交订单：请加入商品多选功能，把选择的商品提交订单)
        </div>
        <table>
            <thead>
            <tr>
                <th>图片</th>
                <th>商品</th>
                <th>价格</th>
                <th>数量</th>
                <th>小计</th>
                <th>操作</th>
                <th>结算</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${productInCarts}" var="cp">
                <tr>
                    <td width="60"><img src="assets/products/${cp.image}"/></td>
                    <td>
                        <a target="_blank" href="products/selectById?pid=${cp.pid}">${cp.pname}</a>
                    </td>
                    <td>￥ ${cp.shopPrice}元</td>
                    <td class="quantity" width="60">
                        <input value="${cp.count}" id='pcount${cp.pid}' onchange='changeCart(${cp.pid})'/>
                    </td>
                    <td width="140"><span class="subtotal">￥${cp.shopPrice*cp.count}元</span></td>
                    <td><a href="javascript:void(0)" onclick="deleteCart(${cp.pid});" class="delete">删除</a></td>
                    <td>
                        <input type="checkbox" checked="checked"
                               value="${cp.pid}" name="buyPids"
                               onchange="countPrice();">
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <dl id="giftItems" class="hidden" style="display: none;">
        </dl>
        <div class="total">
            <em style="font-size: 16pt;font-weight: bold;"> 【已经选中商品的下单统计】 </em>
            商品类别数量: <strong id="countProCate">${countProCate}类</strong>;
            商品数量: <strong id="countPro">${countPro}个</strong>;
            商品总价: <strong id="sumPrice">${sumPrice}元</strong>
        </div>
        <div class="bottom">
            <a href="####" class="clear">清空购物车</a>

            <a href="javascript:void(0)" onclick="commitCart()" class="submit">结算购物车</a>
        </div>

    </div>
    <%--<div class="span24">
        <div class="step step1">
            <span> <h2>亲!您还没有购物!请先去购物!</h2></span>
        </div>
    </div>--%>
</div>

<%@ include file="common-footer.jsp" %>
</body>
</html>

<script>

    // 从购物车中删除
    function deleteCart(pid) {
        // 取出 cookie
        var cookie = decodeURIComponent(document.cookie);
        // 解析 cookie
        var cart = JSON.parse(cookie.substring(cookie.indexOf("=") + 1));
        // 2. 判断商品是否存在
        for (var i = 0; i < cart.length; i++) {
            if (cart[i].pid == pid) {
                // 存在, 移除
                cart.shift();
                // 更新 cookie 信息
                Cookie.setCookie("cart", encodeURIComponent(JSON.stringify(cart)));
                window.open("carts/cart", "_self");
            }
        }
    }

    // 修改输入框的值后, 动态改变购物车的数量
    function changeCart(pid) {
        var cookie = decodeURIComponent(document.cookie);
        var cart = JSON.parse(cookie.substring(cookie.indexOf("=") + 1));
        // 2. 判断商品是否存在
        for (var i in cart) {
            if (cart[i].pid == pid) {
                // 存在, 修改总数量
                cart[i].count = parseInt($("#pcount"+ pid).val());
                // 更新 cookie 信息
                Cookie.setCookie("cart", encodeURIComponent(JSON.stringify(cart)));
            }
        }
    }
</script>