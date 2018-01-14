<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>下单页</title>
    <%@ include file="common-base.jsp" %>
    <link href="assets/css/cart.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<%@ include file="common-header.jsp" %>
<div class="container cart">
    <div class="span24">
        <div class="step step1" style="color: green;font-size: 20pt;font-weight: bold;">
            订单信息
        </div>
        <table>
            <thead>
            <tr>
                <th>图片</th>
                <th>商品</th>
                <th>价格</th>
                <th>数量</th>
                <th>小计</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${productsInOrder}" var="cp">
                <tr>
                    <td width="60"><img src="assets/products/${cp.image}"/></td>
                    <td>
                        <a target="_blank" href="products/selectById?pid=${cp.pid}">${cp.pname}</a>
                    </td>
                    <td>￥ ${cp.shopPrice}元</td>
                    <td class="quantity" width="60">${cp.count}</td>
                    <td width="140"><span class="subtotal">￥${cp.shopPrice*cp.count}元</span></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <%--结算表单--%>
        <form action="order/checkIn?ids=${ids}" method="post">
            <dt>
                收获地址:
            </dt>
            <div>
                <select name="address">
                    <c:forEach items="${userAddresses}" var="address">
                        <option value="${address.id}">地址: ${address.addr}, 电话: ${address.phone},
                            收货人: ${address.name}</option>
                    </c:forEach>
                </select>
            </div>

            <dt>
                结算方式:
            </dt>
            <div>
                <input type="radio" name="payMethod" value="alipay">支付宝
                <input type="radio" name="payMethod" value="wechat">微信
            </div>

            <div class="total">
                <em style="font-size: 16pt;font-weight: bold;"> 订单总价: ${sumPrice}元 </em>
            </div>
            <div class="bottom">
                <a href="javascript:void(0)" onclick="commitOrder()" class="submit">下单</a>
            </div>
        </form>


    </div>
</div>

<%@ include file="common-footer.jsp" %>
</body>
</html>

<script>

    // 提交订单
    function commitOrder() {
        $("form").eq(1).submit();
    }
</script>