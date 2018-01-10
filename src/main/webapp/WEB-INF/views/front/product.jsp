<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>商品详情页</title>
    <!-- 基本页面-元信息 -->
    <%@ include file="common-base.jsp" %>
    <link href="assets/css/product.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<!-- 头部页面 -->
<%@ include file="common-header.jsp" %>

<div class="container productContent">
    <div class="span6">
        <%-- 商品分类导航: 包括二级菜单--%>
        <div class="hotProductCategory">
            <dl>
                <%--父节点--%>
                <c:forEach var="cat" items="${allCategories}">
                    <dt>
                        <a href="products/selectProductWithCategory?cid=${cat.cid}">${cat.cname }</a>
                    </dt>
                    <%--子结点--%>
                    <c:forEach var="catSe" items="${allSecondCategories}">
                        <c:if test="${catSe.category.cid == cat.cid}">
                            <dd>
                                <a href="products/selectProductWithCategorySecond?csid=${catSe.csid}">${catSe.csname }</a>
                            </dd>
                        </c:if>
                    </c:forEach>
                </c:forEach>
            </dl>
        </div>
    </div>
    <div class="span18 last">
        <div class="productImage">
            <a title="" style="outline-style: none; text-decoration: none;"
               id="zoom"
               href="assets/image/r___________renleipic_01/bigPic1ea8f1c9-8b8e-4262-8ca9-690912434692.jpg"
               rel="gallery">
                <div class="zoomPad">
                    <img style="opacity: 1;" class="medium"
                         src="assets/products/${curProduct.image}"/>
                    <div
                            style="display: block; top: 0px; left: 162px; width: 0px; height: 0px; position: absolute; border-width: 1px;"
                            class="zoomPup"></div>
                    <div
                            style="position: absolute; z-index: 5001; left: 312px; top: 0px; display: block;"
                            class="zoomWindow">
                        <div style="width: 368px;" class="zoomWrapper">
                            <div style="width: 100%; position: absolute; display: none;"
                                 class="zoomWrapperTitle"></div>
                            <div style="width: 0%; height: 0px;" class="zoomWrapperImage">
                                <img src=""
                                     style="position: absolute; border: 0px none; display: block; left: -432px; top: 0px;"/>
                            </div>
                        </div>
                    </div>
                    <div
                            style="visibility: hidden; top: 129.5px; left: 106px; position: absolute;"
                            class="zoomPreload">Loading zoom
                    </div>
                </div>
            </a>

        </div>
        <div class="name">${curProduct.pname}</div>
        <div class="sn">
            <div>编号： ${curProduct.pid}</div>
        </div>
        <div class="info">
            <dl>
                <dt>商城价:</dt>
                <dd>
                    <strong>￥：${curProduct.shopPrice}元 </strong> 参 考 价：
                    <del> ￥ ${curProduct.marketPrice} 元</del>
                </dd>
            </dl>
            <dl>
                <dt>促销:</dt>
                <dd>
                    <a target="_blank" title="限时抢购 (2014-07-30 ~ 2015-01-01)">限时抢购</a>
                </dd>
            </dl>
        </div>
        <form id="cartForm" method="post">
            <input type="hidden" name="pid" value="${curProduct.pid}"/>
            <div class="action">
                <dl class="quantity">
                    <dt>购买数量:</dt>
                    <dd>
                        <input id="pcount" value="1" maxlength="4"
                               onpaste="return false;" type="text"/>
                    </dd>
                    <dd>件</dd>
                </dl>
                <div class="buy">
                    <input id="addCart" class="addCart" value="加入购物车" type="button"
                           onclick="saveCart();"/>
                    <br>
                    <input class="addCart" value="购物车结算" type="button"
                           onclick="countCart();"/>
                    <!-- <input id="buyProduct" class="addCart" value="立即购买" type="button"
                    onclick="buyProduct();" /> -->
                </div>
            </div>
        </form>
        <div id="bar" class="bar">
            <ul>
                <li id="introductionTab"><a href="#introduction">商品介绍</a></li>

            </ul>
        </div>

        <div id="introduction" class="introduction">
            <div class="title">
                <strong>${curProduct.pdesc}</strong>
            </div>
            <div style="text-align:center">
                <img src="assets/products/${curProduct.image}"/>
            </div>
            <br/>
        </div>
    </div>
</div>
<!-- 底部页面 -->
<%@ include file="common-footer.jsp" %>

</body>
</html>