<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>物流信息</title>
    <%@ include file="common-base.jsp" %>
</head>
<body>
<%@ include file="common-header.jsp" %>
<div class="container cart">
    <div class="span24">
        <div class="step step1" style="color: green;font-size: 20pt;font-weight: bold;">
            物流信息
        </div>
        <table>
            <thead>
            <tr>
                <th>物流编号:${logisticsInfo.number}</th>
                <th>物流公司:${logisticsInfo.type}</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>时间</td>
                <td>状态</td>
            </tr>
            <c:forEach items="${logisticsInfo.results}" var="info">
                <tr>
                    <td>${info.time}</td>
                    <td>${info.status}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div>
</div>
<%@ include file="common-footer.jsp" %>
</body>
</html>

