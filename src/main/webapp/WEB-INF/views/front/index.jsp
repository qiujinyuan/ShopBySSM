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

<script>
    var chat = {};
    chat.uri = "/chat";

    // 打开聊天窗口之后, 才建立连接; 刷新页面后, 断开连接
    function initChatWin() {
        //显示窗口
        document.getElementById('light').style.display = 'block';
        document.getElementById('fade').style.display = 'block';
        // 如果 chat.socket 不为 null, 表示已经建立过连接, 不在建立连接
        if (!chat.socket) {
            if (window['WebSocket']) {
                chat.socket = new WebSocket("ws://" + window.location.host + chat.uri);
            } else if (window["MozWebSocket"]) {
                chat.socket = new MozWebSocket("ws://" + window.location.host + chat.uri);
            } else {
                alert("不支持 WebSocket 聊天");
            }
        }
        chat.socket.onmessage = onMessage;
        chat.socket.onclose = onClose;
    }

    // chat.socket.onmessage = function (message) {
    // 接收消息, 根据 dataType 判断类型
    function onMessage(message) {
        var msg = JSON.parse(message.data);
        // eval("var msg = " + message.data);
        if (msg.dataType === "userList") {
            // 返回的是用户列表
            var userListArr = msg["userList"];
            // 列表区
            var $userList = $(".chat03_content").find("ul");
            // 清空之前的列表
            $userList.find("li").each(function () {
                $(this).remove();
            });
            // 加载新的列表
            for (var i in userListArr) {
                var $li = $("<li>").appendTo($userList);
                var online = userListArr[i].online == true ? "online" : "offline";
                $("<label class='" + online + "'></lable>").appendTo($li);
                var $a1 = $("<a href='javascript:;'></a>").appendTo($li);
                $("<img src='assets/chat/img/head/" + userListArr[i].img + "'>").appendTo($a1);
                var $a2 = $("<a href='javascript:;' onclick='setClickShowMsgWin(this)' >" + userListArr[i].name + "</a>").appendTo($li);
                // 存入用户相关信息
                $li.attr("userId", userListArr[i].uid);
                $li.attr("userName", userListArr[i].name);
                $li.attr("userType", userListArr[i].type);
                // 用户头像
                $li.attr("img", userListArr[i].img);
                $a2.addClass("chat03_name");
            }
        } else if (msg.dataType === "allNoReadMsgCount") {
            // 收到消息后, 需要在页面上进行提醒: 最新消息数量
            // 收到消息数量类型的通知
            // 总消息数量
            var allCount = msg["allCount"];

            function noticeAllCount() {
                new jBox('Notice', {
                    content: "收到" + allCount + "条未读消息",
                    color: 'blue',
                    autoClose: false
                });
            }

            noticeAllCount();

            // 在页面上进行提示, 显示通知条
            // 与每个用户之间的未读消息数量
            var userListCount = msg["userListCount"];
            for (var i in userListCount) {
                function everyUserNotice() {
                    new jBox('Notice', {
                        content: i + " 发送来 " + userListCount[i] + " 条消息",
                        color: 'red',
                        autoClose: 5000
                    });
                }

                everyUserNotice();
            }
        } else if (msg.dataType === "noReadMsgList") {
            var noReadMsgListArr = msg["noReadMsgList"];
            for (var i in noReadMsgListArr) {
                var $userListLi = $(".chat03_content li");
                var $dom;
                $userListLi.each(function () {
                    if ($(this).attr("userId") == noReadMsgListArr[i].sendUserId) {
                        $dom = $(this);
                    }
                });

                // 往对话框中添加从服务器获取到的消息
                // liIndex 为对话框的索引, 根据 userId 在用户列表中进行遍历, 可以获取到对应的 dom 元素,
                // 然后 var b = dom.index() + 1; 即可找到对应的对话框, 最后把内容添加进去
                var liIndex = $dom.index() + 1,
                    //发送人头像图片
                    sendUserImg = "assets/chat/img/head/" + $dom.attr("img"),
                    //接收人头像图片; 保存在了列表中, 可以根据 dom 元素获取到
                    receiveUserImg = "assets/chat/img/head/2015.jpg",
                    sendUserName = $dom.attr('userName');// 通过 dom 元素获取
                var sendDate = new Date();
                sendDate.setTime(noReadMsgListArr[i].sendDate);

                var sendDateFormat = "";
                sendDateFormat += sendDate.getFullYear() + "-";
                sendDateFormat += sendDate.getMonth() + 1 + "-";
                sendDateFormat += sendDate.getDate() + "  ";
                sendDateFormat += sendDate.getHours() + ":";
                sendDateFormat += sendDate.getMinutes() + ":";
                sendDateFormat += sendDate.getSeconds();

                // 消息内容: 通过 userType 进行判断, 构建不同的消息元素
                var msgHtml = "<div class='message clearfix'><div class='user-logo'><img src='" + sendUserImg + "'/>"
                    + "</div>" + "<div class='wrap-text'>" + "<h5 class='clearfix'>" + sendUserName + "</h5>"
                    + "<div>" + noReadMsgListArr[i].msgContent + "</div>" + "</div>" + "<div class='wrap-ri'>" +
                    "<div clsss='clearfix'><span>" + sendDateFormat + "</span></div>" + "</div>" +
                    "<div style='clear:both;'></div></div>";
                // 消息是否为空
                if (null != noReadMsgListArr[i].msgContent && "" != noReadMsgListArr[i].msgContent) {
                    // 添加消息
                    // 找到对应的消息内容框添加消息
                    // 每个用户对应一个对话框, 根据用户所在的列可以获取到
                    $(".mes" + liIndex).append(msgHtml);
                    // 消息内容框滚动条制定
                    $(".chat01_content").scrollTop($(".mes" + liIndex).height());
                }
            }
        }

    }


    // 连接关闭的时候执行的任务
    function onClose() {
        alert("连接断开, 服务器关闭");
    }

    // 发出下线请求
    function sendLoginOutMsg() {
        if (chat.socket) {
            var outMsg = {outMsg: "outMsg"};
            chat.socket.send(JSON.stringify(outMsg));
        }
    }


    // 点击用户列表某一个用户时, 显示聊天的所有消息, 并在数据库中标记该消息已读


</script>