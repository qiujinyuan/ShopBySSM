<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="assets/chat/css/chat.css"/>

<!--聊天界面的基础js：控制界面效果 -->
<script type="text/javascript" src="assets/chat/js/chat.js"></script>
<script type="text/javascript" src="assets/chat/js/chat_msg.js"></script>

<style>
    .white_content {
        display: none;
        position: fixed;
        top: 50%;
        margin-left: -300px;
        left: 50%;
        margin-top: -160px;
        padding: 0px;
        border: 2px solid orange;
        background-color: #FFF;
        z-index: 10001;
        overflow: auto;
        /*边框虚化*/
        -webkit-box-shadow: #666 0px 0px 20px;
        -moz-box-shadow: #666 0px 0px 20px;
        box-shadow: #666 0px 0px 20px;
    }

    .showChatBtn {
        position: fixed;
        right: 150px;
        top: 100px;
        font-weight: bold;
        width: 50px;
    }
</style>
