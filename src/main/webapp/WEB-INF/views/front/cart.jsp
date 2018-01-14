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
            <tbody id="tab">
            <c:forEach items="${productsInCart}" var="cp">
                <tr>
                    <td width="60"><img src="assets/products/${cp.image}"/></td>
                    <td>
                        <a target="_blank" data-pid="${cp.pid}" href="products/selectById?pid=${cp.pid}">${cp.pname}</a>
                    </td>
                    <td data-shopPrice="${cp.shopPrice}">￥ ${cp.shopPrice}元</td>
                    <td class="quantity" width="60">
                        <input value="${cp.count}" id='pcount${cp.pid}' onchange='changeCart(${cp.pid})'/>
                    </td>
                    <td width="140"><span class="subtotal">￥${cp.shopPrice*cp.count}元</span></td>
                    <td><a href="javascript:void(0)" onclick="deleteCart(${cp.pid});" class="delete">删除</a></td>
                    <td>
                        <input type="checkbox" checked="checked"
                               value="${cp.pid}"
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
            商品类别数量: <strong id="countProCate">${productsInfoInCart.countProCate}类</strong>;
            商品数量: <strong id="countPro">${productsInfoInCart.countPro}个</strong>;
            商品总价: <strong id="sumPrice">${productsInfoInCart.sumPrice}元</strong>
        </div>
        <div class="bottom">
            <a href="javascript:void(0)" class="clear" onclick="clearCart()">清空购物车</a>

            <a href="javascript:void(0)" id="commitCart" onclick="commitCart(this)" class="submit">结算购物车</a>
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
        // 解析 cookie
        var cart = JSON.parse(decodeURIComponent(Cookie.getCookie("cart")));
        // id 相等则删除
        for (var i = 0; i < cart.length; i++) {
            if (cart[i].pid == pid) {
                // 存在, 移除
                cart.splice(i, 1);
                // 更新 cookie 信息
                Cookie.setCookie("cart", encodeURIComponent(JSON.stringify(cart)));
                window.open("carts/cart", "_self");
            }
        }
    }

    // 修改输入框的值后, 动态改变购物车的数量
    function changeCart(pid) {
        var cart = JSON.parse(decodeURIComponent(Cookie.getCookie("cart")));
        // id 相等则修改数量
        for (var i in cart) {
            if (cart[i].pid == pid) {
                // 存在, 修改总数量
                cart[i].count = parseInt($("#pcount" + pid).val());
                // 更新 cookie 信息
                Cookie.setCookie("cart", encodeURIComponent(JSON.stringify(cart)));
                window.open("carts/cart", "_self");
            }
        }
    }

    // 复选框选中则结算:
    // 因为需要绑定选中的数据到提交按钮上, 如果不点击复选框, 直接提交则无参数, 所以需要在页面加载完成后, 执行一次
    $(document).ready(countPrice());

    function countPrice() {
        // 重新计算
        var countProCate = 0;
        var countPro = 0;
        var sumPrice = 0;
        // 被选中的商品的 id
        var ids = "";
        // 被选中的商品的数量
        var counts = "";
        var $trs = $("#tab").find("tr");
        $trs.each(function () {
            var $tds = $(this).find("td");
            if ($tds.eq(6).find("input").eq(0).attr("checked") === "checked") {
                countProCate++;
                // 被选中的商品的 id
                ids += $tds.eq(1).find("a").eq(0).data("pid") + ",";
                // 被选中的商品的价格
                var shopPrice = parseFloat($tds.eq(2).attr("data-shopPrice"));
                // 被选中的商品的数量
                var count = parseInt($tds.eq(3).find("input").eq(0).val());
                counts += count + ",";
                countPro += count;
                sumPrice += count * shopPrice;
            }
        });

        // 将选中的商品 id 保存在结算按钮上
        $("#commitCart").data("ids", ids.substr(0, ids.length - 1));
        /*// 将选中的商品的数量保存在结算按钮上
        $("#commitCart").data("counts", counts.substr(0, counts.length - 1));*/

        // 设置值
        $("#countProCate").html(countProCate + "类");
        $("#countPro").html(countPro + "个");
        $("#sumPrice").html(sumPrice + "元");
    }


    // 清空购物车
    function clearCart() {
        // 设置为新的数组
        var cart = [];
        Cookie.setCookie("cart", encodeURIComponent(JSON.stringify(cart)));
        window.open("carts/cart", "_self");
    }

    // 结算购物车
    function commitCart(obj) {
        // 判断用户是否登陆, 未登录则跳转到登陆页面
        if (${curUser == null}) {
            window.open("shop/login", "_self");
            return;
        }

        // 用户已登陆, 则跳转到订单页, 携带参数为
        // 选中的商品的id
        var ids = $(obj).data("ids");
        // todo 购物车没有商品: 阻止提交
        window.open("order/checkIn?ids=" + ids, "_self");
    }

</script>