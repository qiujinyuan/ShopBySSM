package com.cdsxt.web.chat;

import com.cdsxt.config.GetHttpSessionConfigurator;
import com.cdsxt.ro.ChatInfo;
import com.cdsxt.ro.User;
import com.cdsxt.service.ChatService;
import com.cdsxt.util.JsonUtil;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
public class ChatController {

    private HttpSession httpSession;

    // 使用线程安全的 Hashtable
    public static Map<User, Session> sessionMap = new Hashtable<>();

    // 获取 bean
    private ChatService chatService = (ChatService) ContextLoader.getCurrentWebApplicationContext().getBean(ChatService.class);

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        System.out.println(session.getId() + "建立新连接");

        // 保存到成员变量
        httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        User curUser = this.getCurUser();
        // 为空表示尚未登陆
        if (Objects.nonNull(curUser)) {
            // 修改用户在线状态为 true
            curUser.setOnline(true);
            // 保存到登录用户列表中
            sessionMap.put(curUser, session);
            // 用户上线, 通知所有在线用户, 发送用户列表, 包括当前用户
            for (Map.Entry<User, Session> entry : sessionMap.entrySet()) {
                entry.getValue().getBasicRemote().sendText(this.getUserList(entry.getKey()));
                if (entry.getKey().equals(curUser)) {
                    //  给当前登录用户发送 "登陆成功" 通知
                    String systemNotice = "{\"dataType\":\"systemNotice\", " +
                            "\"msg\":\"" + curUser.getName() + " 登陆成功!\""  + "}";
                    entry.getValue().getBasicRemote().sendText(systemNotice);
                } else {
                    //  给其他用户发送 "xx上线" 通知
                    String systemNotice = "{\"dataType\":\"systemNotice\", " +
                            "\"msg\":\"" + curUser.getName() + " 已上线!\""  + "}";
                    entry.getValue().getBasicRemote().sendText(systemNotice);
                }
            }
        }
    }

    // 获取当前登陆用户
    private User getCurUser() {
        // 获取当前登陆用户
        return (User) httpSession.getAttribute("curUser");
    }

    // 返回用户列表字符串
    private String getUserList(User curUser) {
        List<User> userList = new ArrayList<>();
        if (curUser.getType().equals("backUser")) {
            // 客服
            // 获取用户列表: 其中包含是否登陆状态, 需要与当前登陆用户列表进行对比
            userList = chatService.getUserListWithState("frontUser", sessionMap.keySet());
        } else if (curUser.getType().equals("frontUser")) {
            // 用户
            // 获取客服列表
            userList = chatService.getUserListWithState("backUser", sessionMap.keySet());
        }
        // 拼接 json 字符串
        return "{\"dataType\":\"userList\", \"userList\":" + JsonUtil.objToJsonStr(userList) + "}";
    }

    // 前台传给后台的数据
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("客服端 " + session.getId() + " 发送消息: " + message);
        User curUser = this.getCurUser();
        Map<String, Object> msgs = JsonUtil.jsonStrToMap(message);
        if (msgs != null) {
            // map 中第一个元素总是 dataType, 根据数据类型进行不同的处理
            if ("sendMessage".equals(msgs.get("dataType"))) {
                // 收到客户端的消息
                // 1.保存到数据库中
                this.chatService.storeMessage(curUser, msgs);
                // 2.将消息发送给接收人: 需要发送一个未读消息数量给指定用户, 包括所有消息
                // {
                // dataType：'allNoReadMsgCount',
                // allCount：8,
                // userListCount：{
                // 客服aid ：2
                // 客服bid ：3
                // 客服cid ：3
                // }
                // }

                // 构建接受用户对象
                User receiveUser = this.establishReceiveUser(msgs);

                // 从 sessionMap 中找到该用户
                // 需要接收人在线才能发送消息
                if (sessionMap.containsKey(receiveUser)) {
                    sessionMap.get(receiveUser).getBasicRemote().sendText(this.getCountNoRead(receiveUser));
                }

            } else if ("outMsg".equals(msgs.get("dataType"))) {
                // 如果收到下线通知, 则从 map 中移除
                // 从 map 中移除登陆用户
                sessionMap.remove(curUser);
            } else if ("updateMessageRead".equals(msgs.get("dataType"))) {
                // 收到更新两用户之间消息状态的通知

                // 因为是登陆用户点击列表进行查看, 所有更新状态时: 只能更新当前登录用户做为接收人时, 与发送人的未读消息状态
                // 而当前登录用户做为发送人的消息状态不能被更新
                User receiveUser = this.getCurUser();
                User sendUser = this.establishSendUser(msgs);


                // 1. 先获取到两用户之间的所有未读消息返回, 返回给当前登陆用户, 即接收人
                // {
                // dataType：'noReadMsgList',
                // noReadMsgList：[
                // { sendUserId,xxx,receiveUserId:xxx,msgContent:xxx....}, // 可能不止一条消息
                // { sendUserId,xxx,receiveUserId:xxx,msgContent:xxx....},
                // { sendUserId,xxx,receiveUserId:xxx,msgContent:xxx....}
                // ]
                // }
                //  返回所有未读消息, 那么已读消息何时显示? 在聊天记录中显示
                List<ChatInfo> allNoReadChatInfo = this.chatService.getAllNoReadChatInfoTwoUser(sendUser, receiveUser);
                String noReadMsgList = "{\"dataType\":\"noReadMsgList\", " +
                        "\"noReadMsgList\":" + JsonUtil.objToJsonStr(allNoReadChatInfo) + "}";
                sessionMap.get(receiveUser).getBasicRemote().sendText(noReadMsgList);

                // 2. 再到数据库更新状态为已读
                this.chatService.updateMessageRead(sendUser, receiveUser);
            } else if ("allMsgList".equals(msgs.get("dataType"))) {
                // 查看两个用户之间所有的聊天信息
                // 此时, 当前登陆用户作为发送人, 在数据库并不区分接收人和发送人
                User receiveUser = this.establishReceiveUser(msgs);
                User sendUser = this.getCurUser();
                List<ChatInfo> allMsgChatInfoList = this.chatService.getAllMsgChatInfoTwoUser(sendUser, receiveUser);
                String allMsgList = "{\"dataType\":\"allMsgList\", " +
                        "\"allMsgList\":" + JsonUtil.objToJsonStr(allMsgChatInfoList) + "}";
                sessionMap.get(sendUser).getBasicRemote().sendText(allMsgList);
            }
        }
    }


    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("客服端 " + session.getId() + " 连接断开");
        // 用户下线: 从 map 移除对象, 因为查询时, 是用当前 sessionMap 中保存的所有在线用户与所有用户进行对比, 如果在 map 中表示该用户在线
        // 从 map 中移除, 则即可下线
        // 如果是发下线请求(同时退出登陆), 则当前 sessionMap 中已不存在当前用户; 而且 session 作用域中已经移除当前用户
        // 如果只是下线请求, 而没有退出登录, 则 session 作用域存在当前用户, 而 sessionMap 中不存在
        // 如果没有发出下线请求, 但是刷新页面情况下, 会自动下线, 此时 sessionMap 和 session 作用域中均存在当前用户, 需要移除
        // 直接关闭浏览器或是关闭页面时, 会执行 onClose() 方法, 此时 session 作用域和 sessionMap 中均存在该用户
        /*User curUser = this.getCurUser();
        if (Objects.nonNull(curUser) && sessionMap.containsKey(curUser)) {
            sessionMap.remove(curUser);
        }*/

        User curUser = null;

        // 根据 session 去获取到对应的 User, 则不会遇到 curUser 为 null 的情况
        // 但是有可能只是关闭 session, 而没有往 sessionMap 中存储
        for (Map.Entry<User, Session> entry : sessionMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                curUser = entry.getKey();
            }
        }
        if (Objects.nonNull(curUser)) {
            // 1. 移除对象
            sessionMap.remove(curUser);
            // 2. 给其他用户发送 "xx下线" 通知
            // 3. 遍历 map 发送消息, 更新其他用户的列表
            for (Map.Entry<User, Session> entry : sessionMap.entrySet()) {
                String systemNotice = "{\"dataType\":\"systemNotice\", " +
                        "\"msg\":\"" + curUser.getName() + "已下线!\""  + "}";
                entry.getValue().getBasicRemote().sendText(systemNotice);
                entry.getValue().getBasicRemote().sendText(this.getUserList(entry.getKey()));
            }
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("客服端 " + session.getId() + " 连接出错");
        error.printStackTrace();
    }

    // 返回消息接收人的所有未读消息包括总数量和与每个用户的未读消息数量
    private String getCountNoRead(User receiveUser) {
        // 所有未读消息总数量
        Integer allCount = this.chatService.countChatInfoNoRead(receiveUser.getUid());
        // 与每个用户的未读消息数量
        String userListCount = this.chatService.countChatInfoWithEveryUser(receiveUser);
        String allNoReadMsgCount = "{\"dataType\":\"allNoReadMsgCount\", " +
                "\"allCount\":" + allCount + ", " +
                "\"userListCount\":" + userListCount + "}";
        return allNoReadMsgCount;
    }

    // 根据前台发送的字符串转换成的 map 构建 User 对象
    private User establishReceiveUser(Map<String, Object> msgs) {
        Integer receiveUserId = (Integer) msgs.get("receiveUserId");
        String receiveUserType = (String) msgs.get("receiveUserType");
        User user = new User();
        // 通过 id 和用户类型即可唯一确定一个 User 对象
        user.setType(receiveUserType);
        user.setUid(receiveUserId);
        return user;
    }

    // 根据前台发送的字符串构建发送人 User 对象
    private User establishSendUser(Map<String, Object> msgs) {
        Integer sendUserId = (Integer) msgs.get("sendUserId");
        String sendUserType = (String) msgs.get("sendUserType");
        User user = new User();
        // 通过 id 和用户类型即可唯一确定一个 User 对象
        user.setType(sendUserType);
        user.setUid(sendUserId);
        return user;
    }


}
