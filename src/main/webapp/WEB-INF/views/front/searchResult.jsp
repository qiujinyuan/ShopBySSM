<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>搜索结果</title>

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

<!-- 中间页面 -->
<div class="container index">
    <div class="span24">
        <div id="hotProduct" class="hotProduct clearfix">
            <div class="title">
                <strong>搜索结果</strong>
            </div>
            <ul class="tab">
            </ul>
            <ul class="tabContent" style="display: block;">
                <c:forEach items="${searchProducts}" var="pro">
                    <li>
                        <a href="products/selectById?pid=${pro.pid}">
                            <img src="assets/products/${pro.image}" style="display: block;"/>
                            <strong style="color: #ef0101;">￥：${pro.shopPrice}元 </strong><br/>
                            <strong>${pro.pname} </strong><br/>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>

</div>
<!-- 底部页面  -->
<%@ include file="common-footer.jsp" %>
</body>
</html>