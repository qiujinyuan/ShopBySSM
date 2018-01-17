<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>订单信息</title>
    <%@ include file="../front/common-base.jsp" %>
    <link href="assets/css/cart.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<%@ include file="../front/common-header.jsp" %>
<div class="container cart">
    <div class="span24" style="color:red; font-size: larger;">
        ${orderInfo}
    </div>
</div>

<%@ include file="../front/common-footer.jsp" %>
</body>
</html>
