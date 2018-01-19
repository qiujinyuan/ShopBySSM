var chat = {};
chat.uri = "/chat";
// if (window['WebSocket']) {
//     chat.socket = new WebSocket("ws://" + window.location.host + chat.uri);
// } else if (window["MozWebSocket"]) {
//     chat.socket = new MozWebSocket("ws://" + window.location.host + chat.uri);
// } else {
//     alert("不支持 WebSocket 聊天");
// }


// function initChatWin(){
//     //显示窗口
//     document.getElementById('light').style.display='block';
//     document.getElementById('fade').style.display='block';
//
//     // 建立到服务器的连接
//     chat.socket.onopen = function () {
//
//     };
// }
//
// function openConnect() {
//     var msg = {msg: '请求建立连接'};
//     chat.socket.send(JSON.stringify(msg));
// }