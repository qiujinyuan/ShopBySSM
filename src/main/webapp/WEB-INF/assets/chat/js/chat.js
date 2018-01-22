// 添加消息到对话框
function appendNewMsgToChatWin(curUserName, curUserId, curUserImg) {
    /**
     * 处理表情的方法
     */
    function handleEmoij() {
        -1 != msgContent.indexOf("*#emo_") && (msgContent = msgContent.replace("*#", "<img src='assets/chat/img/").replace("#*", ".gif'/>"), handleEmoij())
    }

    var sendNewMsgDate = new Date();
    var newMsgDateStr = "";
    newMsgDateStr += sendNewMsgDate.getFullYear() + "-";
    newMsgDateStr += sendNewMsgDate.getMonth() + 1 + "-";
    newMsgDateStr += sendNewMsgDate.getDate() + "  ";
    newMsgDateStr += sendNewMsgDate.getHours() + ":";
    newMsgDateStr += sendNewMsgDate.getMinutes() + ":";
    newMsgDateStr += sendNewMsgDate.getSeconds();
    // 取出消息输入框内容
    var msgContent = $("#textarea").val();
    handleEmoij();
    // 消息内容：发送的消息
    var i = "<div class='message clearfix'><div class='user-logo'><img src='assets/chat/img/head/" + curUserImg + "'/>"
        + "</div>" + "<div class='wrap-text'>" + "<h5 class='clearfix'>" + curUserName + "</h5>"
        + "<div>" + msgContent + "</div>" + "</div>" + "<div class='wrap-ri'>" +
        "<div clsss='clearfix'><span>" + newMsgDateStr + "</span></div>" + "</div>" +
        "<div style='clear:both;'></div></div>";
    // 消息是否为空
    if (null != msgContent && "" != msgContent) {
        // 添加消息
        // 找到对应的消息内容框添加消息
        $(".mes" + window.a).append(i);
        // 消息内容框滚动条制定
        $(".chat01_content").scrollTop($(".mes" + window.a).height());
        // 输入框消息置空
        $("#textarea").val("");
        /**
         * 发送消息给服务器
         */
            // 构建消息对象
        var msgEntity = {
                dataType: 'sendMessage',
                receiveUserId: Number.parseInt(curRevUserId),
                receiveUserName: curRevUserName,
                receiveUserType: curRevUserType,
                msgContent: msgContent
            };
        // 发送消息
        chat.socket.send(JSON.stringify(msgEntity));
    } else {
        // 提示输入内容
        alert("请输入聊天内容!");
    }
}

//发送按钮：发送消息事件
function sendBtnMessage(curUserName, curUserId, curUserImg) {
    // 如果没选择用户, 提示
    if (curRevUserId) {
        appendNewMsgToChatWin(curUserName, curUserId, curUserImg);
    } else {
        alert("请选择聊天对象!");
    }
}

$(document).ready(function () {
    /**
     * 发送消息的事件
     */
    // 关闭按钮-隐藏窗口
    $(".close_btn").click(function () {
        $(".chatBox").hide()
    });
    // 用户列表-鼠标移入-选中阴影效果
    $(".chat03_content li").mouseover(function () {
        $(this).addClass("hover").siblings().removeClass("hover")
    }).mouseout(function () {
        $(this).removeClass("hover").siblings().removeClass("hover")
    });

    // 表情框-鼠标移入等事件-显示或关闭
    $(".ctb01").mouseover(function () {
        $(".wl_faces_box").show()
    }).mouseout(function () {
        $(".wl_faces_box").hide()
    });
    $(".wl_faces_box").mouseover(function () {
        $(".wl_faces_box").show()
    }).mouseout(function () {
        $(".wl_faces_box").hide()
    });
    $(".wl_faces_close").click(function () {
        $(".wl_faces_box").hide()
    });

    //选择表情-事件-添加文本输入框表情的【字符内容】
    $(".wl_faces_main img").click(function () {
        var a = $(this).attr("src");
        $("#textarea").val($("#textarea").val() + "*#" + a.substr(a.indexOf("img/") + 4, 6) + "#*"),
            $("#textarea").focusEnd(),
            $(".wl_faces_box").hide()
    });

    //其他效果
    $.fn.setCursorPosition = function (a) {
        return 0 == this.lengh ? this : $(this).setSelection(a, a)
    };
    $.fn.setSelection = function (a, b) {
        if (0 == this.lengh) return this;
        if (input = this[0], input.createTextRange) {
            var c = input.createTextRange();
            c.collapse(!0),
                c.moveEnd("character", b),
                c.moveStart("character", a),
                c.select()
        } else input.setSelectionRange && (input.focus(), input.setSelectionRange(a, b));
        return this
    };
    $.fn.focusEnd = function () {
        this.setCursorPosition(this.val().length)
    }
});

/**
 * 设置点击窗口的【用户列表】的单击事件
 */
function setClickShowMsgWin(obj, isNotice) {
    // 获取 li 元素对象
    var domLi = $(obj);
    //更新切换消息内容框
    updateMsgWin(domLi);
    // 点击后, 更新未读消息数量为 0 条
    updateMsgCount(curRevUserName, 0);
    /**
     *
     * a）读取消息并更新未读消息状态为已读
     */
        // Zxx.readAndUpdateMessageRead(curRevUserId, curRevUserType);
        // 点击后发送请求到服务器, 请求内容为: 获取与该用户的所有未读消息
        // 两个用户之间所有的消息均标记为已读; 数据库查询时, 两个用户不区分接收人和发送人
        // 先获取到当前登陆用户的 id 和 类型: 登陆用户在服务器通过 session 作用域去取
    var updateMessageRead = {
            dataType: 'updateMessageRead',
            sendUserId: Number.parseInt(curRevUserId), // 此时被点击用户做为发送人;
            sendUserType: curRevUserType
        };
    chat.socket.send(JSON.stringify(updateMessageRead));

    // 如果是点击用户列表, 则需要手动把通知条(默认不会自动关闭)关闭
    // 如果是点击通知条, 则系统会自动销毁通知条, 无需手动关闭
    if (!isNotice) {
        closeJBoxNotice(curRevUserName);
    }
}

/**
 * 关闭 jBox 通知条, 并将其从 jBox 数组中移除
 */
function closeJBoxNotice(targetUserName) {
    for (var j in jbs) {
        var str = jbs[j].content["0"].innerHTML;
        if (targetUserName == str.substring(0, str.indexOf(" "))) {
            var jBoxId = $(jbs[j]).attr("id");
            // 移除 jBox dom 元素
            $("#" + jBoxId).remove();
            // 从数组中移除 jBox 对象
            jbs.splice(j, 1);
        }
    }
}

/**
 * 更新切换消息内容框
 */
    //当前接收消息的用户信息: 只有点击用户列表的某个用户后, 以下变量才会被赋值
var curRevUserId, curRevUserName, curRevUserType, curRevUserImg;

function updateMsgWin(domLi) {
    // debugger;
    //更新接收人id
    curRevUserId = domLi.attr('userId');
    curRevUserName = domLi.attr('userName');
    curRevUserType = domLi.attr('userType');
    curRevUserImg = domLi.attr('img');

    //获取点击的位置
    var b = domLi.index() + 1;
    //更新接收人的位置-在获取消息的时候，需要使用a-全局变量
    a = b; // a 为右侧列表的序号
    //更新接收人的图片-
    // c = "assets/chat/img/head/20" + (12 + a) + ".jpg";
    c = domLi.attr('img');
    // d = dom.find(".chat03_name").text();
    d = curRevUserName;

    //消息内容框滚动到底部
    $(".chat01_content").scrollTop($(".mes" + window.a).height());
    //设置自己的选中样式，其他的取消
    domLi.addClass("choosed").siblings().removeClass("choosed");
    //设置聊天框-当前接收人的名字
    $(".talkTo a").text(curRevUserName);
    //当前的消息内容框显示，其他的消息内容框隐藏
    $(".mes" + b).show().siblings().hide();
}

// 更新消息数量: 指定更新哪个发送人的消息数量为多少条
function updateMsgCount(userName, targetCount) {
    var $userListLiCopy = $(".chat03_content li");
    $userListLiCopy.each(function () {
        if ($(this).attr("userName") == userName) {
            // 更改 dom 元素, 进行提示消息条数
            var $a2 = $(this).children("a").eq(1);
            var regex = /\[\w+\]/;
            $a2.html($a2.html().replace(regex, "[" + targetCount + "]"));
        }
    });
}

// 成员变量保存当前用户
var curUserName, curUserId, curUserImg;

// 显示所有的聊天记录
function showAllMsg(curUserNameParam, curUserIdParam, curUserImgParam, e) {
    e.preventDefault();
    if (curRevUserId) {
        // 同时保存当前登陆用户, 用于消息返回之后显示
        curUserName = curUserNameParam;
        curUserId = curUserIdParam;
        curUserImg = curUserImgParam;

        // 构建消息对象
        var msgEntity = {
            dataType: 'allMsgList',
            receiveUserId: Number.parseInt(curRevUserId), // 被点击用户做为另一方用户, 只有点击后, 成员变量才有值, 才能发送消息
            receiveUserType: curRevUserType
        };
        // 发送消息
        chat.socket.send(JSON.stringify(msgEntity));
    } else {
        alert("请选择聊天对象!");
    }
}

