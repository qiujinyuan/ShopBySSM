<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>登陆页面</title>

    <!-- 基本页面-元信息 -->
    <%@ include file="common-base.jsp" %>
    <link href="assets/css/login.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<!-- 头部页面 -->
<%@ include file="common-header.jsp" %>
<br>
<div class="container login">
    <div class="span12">
        <div class="ad">
            <img src="assets/image/login.jpg"
                 width="500" height="330" alt="会员登录" title="会员登录">
        </div>
    </div>
    <div class="span12 last">
        <div class="wrap">
            <div class="main">
                <div class="title">
                    <strong>会员登录</strong>USER LOGIN
                </div>
                <div></div>
                <form id="loginForm"
                      action="shop/login"
                      method="post" novalidate="novalidate">
                    <table>
                        <tbody>
                        <tr>
                            <th>用户名:</th>
                            <td><input type="text" id="username" name="username"
                                       class="text" maxlength="20"/></td>
                        </tr>
                        <tr>
                            <th>密&nbsp;&nbsp;码:</th>
                            <td><input type="password" id="password" name="password"
                                       class="text" maxlength="20" autocomplete="off"/></td>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                            <td><input type="submit" class="submit" value="登 录"/></td>
                        </tr>
                        <tr class="register">
                            <th>&nbsp;</th>
                            <td>
                                <dl>
                                    <dt>还没有注册账号？</dt>
                                    <dd>
                                        立即注册即可体验在线购物！ <a
                                            href="shop/register">立即注册</a>
                                    </dd>
                                </dl>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- 底部页面  -->
<%@ include file="common-footer.jsp" %>
</body>
</html>