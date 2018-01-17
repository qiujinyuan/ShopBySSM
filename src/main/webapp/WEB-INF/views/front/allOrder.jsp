<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>所有订单</title>
    <%@ include file="common-base.jsp" %>
    <link href="assets/css/cart.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<%@ include file="common-header.jsp" %>
<div class="container cart">
    <div class="span24">
        <div class="step step1" style="color: green;font-size: 20pt;font-weight: bold;">
            所有订单
        </div>
        <table>
            <thead>
            <tr>
                <th>订单号</th>
                <th>总价</th>
                <th>下单时间</th>
                <th>收货人</th>
                <th>支付状态</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${allOrders}" var="order">
                <tr>
                    <td>${order.oid}</td>
                    <td>${order.sumPrice}</td>
                    <td class="orderTime">${order.orderTime}</td>
                    <td>${order.name}</td>
                    <td>
                            <%--未支付, 则打开付款页面--%>
                        <c:choose>
                            <c:when test="${order.state == '未支付'}">
                                <a target="_blank" href="alipay/pay?oid=${order.oid}"
                                   style="color:red">${order.state}</a>
                            </c:when>
                            <c:otherwise>
                                ${order.state}
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div>
</div>

<%@ include file="common-footer.jsp" %>
</body>
</html>

<script>
    $(document).ready(function () {
        $(".orderTime").each(function () {
            var gmtDate = new Date($(this).html());
            gmtDate.setTime(gmtDate.getTime() - 14 * 60 * 60 * 1000);
            $(this).html(gmtDate.toLocaleString());
        })
    })
</script>
