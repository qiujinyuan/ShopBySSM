<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 聊天窗口 -->
<div class="content">
    <div id="light" class="chatBox white_content">
        <div class="chatLeft">
            <div class="chat01">
                <div class="chat01_title">
                    <ul class="talkTo">
                        <li><a href="javascript:;"></a></li>
                    </ul>
                    <a class="close_btn" href="javascript:;"></a>
                </div>
                <div class="chat01_content">
                    <c:forEach begin="1" end="10" step="1" var="num">
                        <c:if test="${num==3}" var="numTest">
                            <div class="message_box mes${num}" style="display: block;"></div>
                        </c:if>
                        <c:if test="${!numTest}" var="numTest">
                            <div class="message_box mes${num}"></div>
                        </c:if>
                    </c:forEach>

                </div>
            </div>
            <div class="chat02">
                <div class="chat02_title">
                    <a class="chat02_title_btn ctb01" href="javascript:;"></a><a
                        class="chat02_title_btn ctb02" href="javascript:;" title="选择文件">
                    <embed width="15" height="16"
                           flashvars="swfid=2556975203&amp;maxSumSize=50&amp;maxFileSize=50&amp;maxFileNum=1&amp;multiSelect=0&amp;uploadAPI=http%3A%2F%2Fupload.api.weibo.com%2F2%2Fmss%2Fupload.json%3Fsource%3D209678993%26tuid%3D1887188824&amp;initFun=STK.webim.ui.chatWindow.msgToolBar.upload.initFun&amp;sucFun=STK.webim.ui.chatWindow.msgToolBar.upload.sucFun&amp;errFun=STK.webim.ui.chatWindow.msgToolBar.upload.errFun&amp;beginFun=STK.webim.ui.chatWindow.msgToolBar.upload.beginFun&amp;showTipFun=STK.webim.ui.chatWindow.msgToolBar.upload.showTipFun&amp;hiddenTipFun=STK.webim.ui.chatWindow.msgToolBar.upload.hiddenTipFun&amp;areaInfo=0-16|12-16&amp;fExt=*.jpg;*.gif;*.jpeg;*.png|*&amp;fExtDec=选择图片|选择文件"
                           data="upload.swf" wmode="transparent" bgcolor=""
                           allowscriptaccess="always" allowfullscreen="true" scale="noScale"
                           menu="false" type="application/x-shockwave-flash"
                           src="http://service.weibo.com/staticjs/tools/upload.swf?v=36c9997f1313d1c4"
                           id="swf_3140"/>
                </a> <label class="chat02_title_t"> <a href="####"
                                                       target="_blank">聊天记录</a></label>
                    <div class="wl_faces_box">
                        <div class="wl_faces_content">
                            <div class="title">
                                <ul>
                                    <li class="title_name">常用表情</li>
                                    <li class="wl_faces_close"><span>&nbsp;</span></li>
                                </ul>
                            </div>
                            <div class="wl_faces_main">
                                <ul>
                                    <c:forEach begin="1" end="60" step="1" var="num">
                                        <c:if test="${num<10}" var="numTest">
                                            <li><a href="javascript:;"> <img
                                                    src="assets/chat/img/emo_0${num}.gif"/></a></li>
                                        </c:if>
                                        <c:if test="${!numTest}">
                                            <li><a href="javascript:;"> <img
                                                    src="assets/chat/img/emo_${num}.gif"/></a></li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <div class="wlf_icon"></div>
                    </div>
                </div>
                <div class="chat02_content">
                    <textarea id="textarea"></textarea>
                </div>
                <div class="chat02_bar">
                    <ul>
                        <li style="right: 5px; top: 5px;"><a href="javascript:;">
                            <img src="assets/chat/img/send_btn.jpg"/>
                        </a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="chatRight">
            <div class="chat03">
                <div class="chat03_title">
                    <label class="chat03_title_t">${curUser.name}_消息:</label>
                </div>
                <div class="chat03_content">
                    <ul>
                        <li>正在加载用户列表</li>
                        <li class="choosed">
                            <label class="online">
                            </label>
                            <a href="javascript:;">
                                <img src="assets/chat/img/head/2015.jpg"></a>
                            <a href="javascript:;" class="chat03_name">柳岩</a>
                        </li>
                        <li>
                            <label class="offline">
                            </label>
                            <a href="javascript:;">
                                <img src="assets/chat/img/head/2015.jpg"></a><a href="javascript:;" class="chat03_name">张飞</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div style="clear: both;"></div>
    </div>
    <div id="fade" class="black_overlay"></div>
</div>