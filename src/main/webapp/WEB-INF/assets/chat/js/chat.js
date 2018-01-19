/**
 * 标题有消息提示
 */
function message() {
    var a = $.blinkTitle.show();
    setTimeout(function() {
        $.blinkTitle.clear(a)
    },
    8e3)
}
$(document).ready(function() {
	/**
	 * 发送消息的事件
	 */
    function e() {
    	/**
		 * 处理表情的方法
		 */
        function h() { 
        	- 1 != g.indexOf("*#emo_") && (g = g.replace("*#", "<img src='../img/").replace("#*", ".gif'/>"), h())
        }
        var e = new Date,
        f = "";
        f += e.getFullYear() + "-",
        f += e.getMonth() + 1 + "-",
        f += e.getDate() + "  ",
        f += e.getHours() + ":",
        f += e.getMinutes() + ":",
        f += e.getSeconds();
        // 取出消息输入框内容
        var g = $("#textarea").val();
        h();
        // 消息内容：发送的消息
        var i = "<div class='message clearfix'><div class='user-logo'><img src='" + b + "'/>"
        + "</div>" + "<div class='wrap-text'>" + "<h5 class='clearfix'>"+curUserName+"</h5>" 
        + "<div>" + g + "</div>" + "</div>" + "<div class='wrap-ri'>" +
        "<div clsss='clearfix'><span>" + f + "</span></div>" + "</div>" + 
        "<div style='clear:both;'></div></div>";
		// 消息是否为空
        if(null != g && "" != g ){
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
    		Zxx.sendMsg(g);
    		// 标题消息提示
    		//message();
        }else{
        	// 提示输入内容
    		alert("请输入聊天内容!");
        }
    }
    
    var a = 3,
    //发送人头像图片
    b = "../img/head/2024.jpg",
    //接收人头像图片
    c = "../img/head/2015.jpg",
    d = "对方的名字";
    
    // 关闭按钮-隐藏窗口
    $(".close_btn").click(function() {
        $(".chatBox").hide()
    });
    // 用户列表-鼠标移入-选中阴影效果
    $(".chat03_content li").mouseover(function() {
        $(this).addClass("hover").siblings().removeClass("hover")
    }).mouseout(function() {
        $(this).removeClass("hover").siblings().removeClass("hover")
    });
    
    // 表情框-鼠标移入等事件-显示或关闭
    $(".ctb01").mouseover(function() {
        $(".wl_faces_box").show()
    }).mouseout(function() {
        $(".wl_faces_box").hide()
    });
    $(".wl_faces_box").mouseover(function() {
        $(".wl_faces_box").show()
    }).mouseout(function() {
        $(".wl_faces_box").hide()
    });
    $(".wl_faces_close").click(function() {
        $(".wl_faces_box").hide()
    });
    
    //选择表情-事件-添加文本输入框表情的【字符内容】
    $(".wl_faces_main img").click(function() {
        var a = $(this).attr("src");
        $("#textarea").val($("#textarea").val() + "*#" + a.substr(a.indexOf("img/") + 4, 6) + "#*"),
        $("#textarea").focusEnd(),
        $(".wl_faces_box").hide()
    });
    //发送按钮：发送消息事件
    $(".chat02_bar img").click(function(event) {
        e();
    });

    //键盘enter：发送消息事件
    document.onkeydown = function(a) {
        var b = document.all ? window.event: a;
        return 13 == b.keyCode ? (e(), !1) : void 0
    };
    
    
    //其他效果
    $.fn.setCursorPosition = function(a) {
        return 0 == this.lengh ? this: $(this).setSelection(a, a)
    };
    $.fn.setSelection = function(a, b) {
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
    $.fn.focusEnd = function() {
        this.setCursorPosition(this.val().length)
    }
}),
function(a) {
    a.extend({
        blinkTitle: {
            show: function() {
                var a = 0,
                b = document.title;
                if ( - 1 == document.title.indexOf("【")) var c = setInterval(function() {
                    a++,
                    3 == a && (a = 1),
                    1 == a && (document.title = "【　　　】" + b),
                    2 == a && (document.title = "【新消息】" + b)
                },
                500);
                return [c, b]
            },
            clear: function(a) {
                a && (clearInterval(a[0]), document.title = a[1])
            }
        }
    })
} (jQuery);
/**
 * 设置点击窗口的【用户列表】的单击事件
 */
function setClickShowMsgWin(){
	$(".chat03_content li").click(function() {
		debugger;
		var dom=$(this);
		//更新切换消息内容框
		updateMsgWin(dom);
		/**
		 *
		 * a）读取消息并更新未读消息状态为已读
		 */
		Zxx.readAndUpdateMessageRead(curRevUserId,curRevUserType);
	});
}
/**
 * 更新切换消息内容框
 */
//当前接收消息的id
var curRevUserId,curRevUserName,curRevUserType;
function updateMsgWin(dom){
	debugger;
	//更新接收人id
	curRevUserId=dom.attr('userId');
	curRevUserName=dom.attr('userName');
	curRevUserType=dom.attr('userType');
	
	//获取点击的位置
    var b = dom.index() + 1;
    //更新接收人的位置-在获取消息的时候，需要使用a-全局变量
    a = b;
    //更新接收人的图片-
    c = "../img/head/20" + (12 + a) + ".jpg";
    d = dom.find(".chat03_name").text();
    //消息内容框移动到顶部
    $(".chat01_content").scrollTop(0);
    //设置自己的选中样式，其他的取消
    dom.addClass("choosed").siblings().removeClass("choosed");
    //设置聊天框-当前接收人的名字
    $(".talkTo a").text(curRevUserName);
    //当前的消息内容框显示，其他的消息内容框隐藏
    $(".mes" + b).show().siblings().hide();
}


