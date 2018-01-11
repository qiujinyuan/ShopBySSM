<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="assets/js/cookie-util.js"></script>
<script type="text/javascript">
    function loginOut() {
        //清除cookie购物车
        Cookie.delCookie('cart');
        //请求到后台注销
        window.location.href = "${pageContext.request.contextPath}/user/loginOut.do";
    }
</script>

<div class="container header">
    <div class="span5">
        <div class="logo">
            <a href="shop/front/index.jsp"> <img
                    src="assets/image/r___________renleipic_01/logo.png" alt="尚学堂"/>
            </a>
        </div>
    </div>
    <div class="span9">
        <div class="headerAd">
            <img src="assets/image/header.jpg" width="320" height="50" alt="正品保障"
                 title="正品保障"/>
        </div>
    </div>
    <div class="span10 last">
        <div class="topNav clearfix">
            <ul>
                <c:if test="${curUser==null}">
                    <li id="headerLogin" class="headerLogin"
                        style="display: list-item;"><a href="shop/login">登录</a>|
                    </li>
                    <li id="headerLogin" class="headerLogin"
                        style="display: list-item;"><a href="shop/login">管理员登录</a>|
                    </li>
                    <li id="headerRegister" class="headerRegister"
                        style="display: list-item;"><a href="shop/login">注册</a>|
                    </li>
                </c:if>
                <c:if test="${curUser!=null}">
                    <li id="headerLogin" class="headerLogin"
                        style="display: list-item;"> ${curUser.userName}|
                    </li>
                    <li id="headerLogin" class="headerLogin"
                        style="display: list-item;"><a
                            href="order/query.do">我的订单</a> |
                    </li>
                    <li id="headerRegister" class="headerRegister"
                        style="display: list-item;">
                        <a href="javascript:void(0)" onclick="loginOut();">退出</a>
                    </li>

                </c:if>

                <li><a>会员中心</a> |</li>
                <li><a>购物指南</a> |</li>
                <li><a>关于我们</a> |</li>
                <!-- <li>[欢迎你：]</li> -->
            </ul>
        </div>
        <div class="cart">
            <a href="carts/cart">购物车</a>
        </div>
        <div class="phone">
            尚学堂客服热线: <strong>028-65176856</strong>
        </div>
    </div>
    <%--搜索按钮--%>
    <div class="span24">
        <form action="products/selectProductWithParam" method="post">
            <div class="span6">
                <strong>名称: </strong><input type="text" name="productName">
            </div>
            <div class="span6">
                <strong>价格区间: </strong><input type="text" name="minPrice" style="width:75px"><strong>~</strong>
                <input type="text" name="maxPrice" style="width:75px">
            </div>
            <div class="span6">
                <strong>商品分类: </strong><input type="text" name="categoryName">
            </div>
            <div class="span4" style="text-align: center">
                <input type="submit" value="搜索"/>
            </div>
        </form>
    </div>
    <div class="span24">
        <ul class="mainNav">
            <li><a href="products/index">首页</a> |</li>
            <%--todo 所有商品目录; 一级分类--%>
            <c:forEach var="cat" items="${allCategories}">
                <li>
                    <a href="products/selectProductWithCategory?cid=${cat.cid}">${cat.cname }</a>|
                </li>
            </c:forEach>
        </ul>
    </div>

</div>
<hr width="60%">