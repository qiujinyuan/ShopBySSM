<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>商城首页</title>

    <!-- 基本页面-元信息 -->
    <%@ include file="common-base.jsp" %>


    <script type="text/javascript">
        //查看cookie
        //document.write(document.cookie);
    </script>
</head>
<body>

<!-- 头部页面 -->
<%@ include file="common-header.jsp" %>

<%--聊天页面 js, css 等内容--%>
<%@ include file="../chat/chat_head.jsp" %>
<%--聊天窗口--%>
<%@ include file="../chat/chat_body.jsp" %>
<%--如果用户尚未登陆, 则不显示在线咨询图片--%>
<c:if test="${curUser != null}">
    <%--显示聊天窗口--%>
    <div class="showChatBtn">
        <a href="javascript:void(0);" onclick="initChatWin();">
            <img src="assets/chat/img/chat_log.jpg">
        </a>
    </div>
</c:if>


<!-- 中间页面 -->
<div class="container index">
    <div class="span24">
        <div id="hotProduct" class="hotProduct clearfix">
            <div class="title">
                <strong>热门商品</strong>
                <!-- <a  target="_blank"></a> -->
            </div>
            <ul class="tab">
                <%--商品分类: 二级分类, 比如男装下的裤子或上衣--%>
                <%--商品分类--%>
                <c:forEach var="catSe" items="${allSecondCategories}">
                    <li <%--class="current"--%>>
                        <a href="products/selectProductWithCategorySecond?csid=${catSe.csid}"
                           style="color: white">${catSe.category.cname}.${catSe.csname}</a>
                    </li>
                </c:forEach>
            </ul>
            <ul class="tabContent" style="display: block;">
                <%--显示所有最热商品, 根据 isHot 查询; 然后遍历--%>
                <c:forEach items="${hotProducts}" var="hotPro">
                    <li>
                            <%--点击图片进入详情页--%>
                        <a href="products/selectById?pid=${hotPro.pid}">
                            <img src="assets/products/${hotPro.image}" style="display: block;"/>
                            <strong style="color: #ef0101;">￥：${hotPro.shopPrice}元 </strong><br/>
                            <strong>${hotPro.pname} </strong><br/>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <div class="span24">
        <div id="newProduct" class="newProduct clearfix">
            <div class="title">
                <strong>最新商品(最近三个月)</strong> <a target="_blank"></a>
            </div>
            <ul class="tab">
                <%--商品分类--%>
                <c:forEach var="catSe" items="${allSecondCategories}">
                    <li <%--class="current"--%>>
                        <a href="products/selectProductWithCategorySecond?csid=${catSe.csid}"
                           style="color: white">${catSe.category.cname}.${catSe.csname}</a>
                    </li>
                </c:forEach>
            </ul>
            <ul class="tabContent" style="display: block;">
                <%--单个商品展示--%>
                <%--遍历所有商品, 然后显示图片及相关信息--%>
                <c:forEach items="${newProducts}" var="newPro">
                    <li>
                        <a href="products/selectById?pid=${newPro.pid}">
                            <img src="assets/products/${newPro.image}" style="display: block;"/>
                            <strong style="color: #ef0101;">￥：${newPro.shopPrice}元 </strong><br/>
                            <strong>${newPro.pname} </strong><br/>
                        </a>
                    </li>
                </c:forEach>

            </ul>
            <ul class="tabContent" style="display: none;">

            </ul>
        </div>
    </div>

</div>
<!-- 底部页面  -->
<%@ include file="common-footer.jsp" %>

</body>
</html>
