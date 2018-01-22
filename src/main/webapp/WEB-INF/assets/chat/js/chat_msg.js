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

// 装 jBox
var jbs = [];

// 接收消息, 根据 dataType 判断类型
function onMessage(message) {
    var msg = JSON.parse(message.data);
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
            var $li = $("<li onclick='setClickShowMsgWin(this, false)'>").appendTo($userList);
            var online = userListArr[i].online == true ? "online" : "offline";
            $("<label class='" + online + "'></lable>").appendTo($li);
            var $a1 = $("<a href='javascript:;'></a>").appendTo($li);
            $("<img src='assets/chat/img/head/" + userListArr[i].img + "'>").appendTo($a1);
            var $a2 = $("<a href='javascript:;'>" + userListArr[i].name + " [0]</a>").appendTo($li);
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
                autoClose: 10000
            });
        }

        noticeAllCount();

        // 在页面上进行提示, 显示通知条
        // 与每个用户之间的未读消息数量
        var userListCount = msg["userListCount"];
        // i 为用户名, 值为消息数量
        for (var i in userListCount) {
            // 已存在同名用户的未读消息通知条, 则关闭之, 显示最新的消息通知条(条数最多)
            closeJBoxNotice(i);

            // 函数会按顺序进行覆盖, 导致后面同时显示多个通知时, 后面的通知方法会覆盖前前面的通知, 所以需要使用 this 在函数中取值
            function everyNotice() {
                var jb = new jBox('Notice', {
                    content: i + " 发送来 " + userListCount[i] + " 条消息",
                    color: 'red',
                    autoClose: false,
                    onClose: function () {
                        // 将用户名切割出来
                        var str = this.content["0"].innerHTML;
                        // 关闭时, 可以调用另一个方法, 在其中发送更新消息的请求或者是进行消息提示
                        onCloseGetNoReadMessage(str.substring(0, str.indexOf(" ")));
                    }
                });
                // 添加到 jBox 数组中
                jbs.push(jb);
            }

            everyNotice();

            // 同时更改发送消息用户的未读消息计数
            updateMsgCount(i, userListCount[i]);
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
            if ($dom) {
                // 往对话框中添加从服务器获取到的消息
                // liIndex 为对话框的索引, 根据 userId 在用户列表中进行遍历, 可以获取到对应的 dom 元素,
                // 然后 var b = dom.index() + 1; 即可找到对应的对话框, 最后把内容添加进去
                var liIndex = $dom.index() + 1,
                    //发送人头像图片
                    sendUserImg = "assets/chat/img/head/" + $dom.attr("img"),
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
                    "<div class='clearfix'><span>" + sendDateFormat + "</span></div>" + "</div>" +
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
    } else if (msg.dataType === "allMsgList") {
        // 所有聊天记录
        var allMsgListArr = msg["allMsgList"];
        appendMsgToWin(allMsgListArr);
    } else if (msg.dataType === "systemNotice") {
        // 系统通知消息
        function systemNotice() {
            new jBox('Notice', {
                theme: 'NoticeFancy',
                attributes: {
                    x: 'right',
                    y: 'bottom'
                },
                color: "green",
                content: msg.msg,
                autoClose: 5000,
                audio: 'assets/jbox/audio/bling2',
                animation: {open: 'slide:bottom', close: 'slide:left'}
            });
        }

        systemNotice();
    }
}

// 添加聊天信息到对话框的方法: 显示所有聊天信息
function appendMsgToWin(msgListArr) {
    for (var i in msgListArr) {
        // 往对话框中添加从服务器获取到的消息
        // liIndex 为对话框的索引, 根据 userId 在用户列表中进行遍历, 可以获取到对应的 dom 元素,
        // 然后 var b = dom.index() + 1; 即可找到对应的对话框, 最后把内容添加进去
        var sendDate = new Date();
        sendDate.setTime(msgListArr[i].sendDate);

        var sendDateFormat = "";
        sendDateFormat += sendDate.getFullYear() + "-";
        sendDateFormat += sendDate.getMonth() + 1 + "-";
        sendDateFormat += sendDate.getDate() + "  ";
        sendDateFormat += sendDate.getHours() + ":";
        sendDateFormat += sendDate.getMinutes() + ":";
        sendDateFormat += sendDate.getSeconds();

        // 消息内容: 通过 userType 进行判断, 构建不同的消息元素
        var msgHtml;
        // 通过不同的发送人进行判断, 显示不同的用户头像和姓名
        if (msgListArr[i].sendUserId == curUserId) {
            // 发送人是当前登陆用户
            msgHtml = "<div class='message clearfix'><div class='user-logo'><img src='assets/chat/img/head/" + curUserImg + "'/>"
                + "</div>" + "<div class='wrap-text'>" + "<h5 class='clearfix'>" + curUserName + "</h5>"
                + "<div>" + msgListArr[i].msgContent + "</div>" + "</div>" + "<div class='wrap-ri'>" +
                "<div class='clearfix'><span>" + sendDateFormat + "</span></div>" + "</div>" +
                "<div style='clear:both;'></div></div>";
        } else if (msgListArr[i].sendUserId == curRevUserId) {
            // 发送人是被点击的聊天用户
            msgHtml = "<div class='message clearfix'><div class='user-logo'><img src='assets/chat/img/head/" + curRevUserImg + "'/>"
                + "</div>" + "<div class='wrap-text'>" + "<h5 class='clearfix'>" + curRevUserName + "</h5>"
                + "<div>" + msgListArr[i].msgContent + "</div>" + "</div>" + "<div class='wrap-ri'>" +
                "<div class='clearfix'><span>" + sendDateFormat + "</span></div>" + "</div>" +
                "<div style='clear:both;'></div></div>";
        }

        // 消息是否为空
        if (null != msgListArr[i].msgContent && "" != msgListArr[i].msgContent) {
            // 添加消息
            // 找到对应的消息内容框添加消息
            // 每个用户对应一个对话框, 根据用户所在的列可以获取到
            $(".mes" + window.a).append(msgHtml);
            // 消息内容框滚动条到最高点
            $(".chat01_content").scrollTop(0);
        }
    }
}

// 在关闭通知条时, 获取到接收人和发送人等, 然后发送
function onCloseGetNoReadMessage(sendUserName) {
    var $userListLi = $(".chat03_content li");
    // 获取到用户列表中对应的 dom 元素
    var domInUserList;
    $userListLi.each(function () {
        if ($(this).attr("userName") == sendUserName) {
            domInUserList = this;
        }
    });
    if (domInUserList) {
        setClickShowMsgWin(domInUserList, true);
    }
    // 发送请求之后, 会再次回到聊天窗口, 进行未读消息的显示
    // 在 updateMsgWin() 方法中会显示对应的聊天窗口
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