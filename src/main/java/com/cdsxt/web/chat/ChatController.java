package com.cdsxt.web.chat;

import com.cdsxt.config.GetHttpSessionConfigurator;
import com.cdsxt.ro.User;
import com.cdsxt.service.ChatService;
import com.cdsxt.util.JsonUtil;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
public class ChatController {

    private HttpSession httpSession;

    // 使用线程安全的 Hashtable
    private static Map<User, Session> sessionMap = new Hashtable<>();

    // 获取 bean
    private ChatService chatService = (ChatService) ContextLoader.getCurrentWebApplicationContext().getBean(ChatService.class);

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        System.out.println(session.getId() + "建立新连接");

        // 保存到成员变量
        httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        User curUser = this.getCurUser();
        // 修改用户在线状态为 true
        curUser.setOnline(true);
        // 保存到登录用户列表中
        sessionMap.put(curUser, session);
        // 返回用户列表
        session.getBasicRemote().sendText(this.getUserList(curUser));
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
        // String userListStr = JsonUtil.objToJsonStr(userList);
        // 拼接 json 字符串
        return "{\"dataType\":\"userList\", \"userList\":" + JsonUtil.objToJsonStr(userList) + "}";
    }

/*    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("客服端 " + session.getId() + " 发送消息: " + message);
        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
            if (!entry.getKey().equals(session.getId())) {
                // 指定用户发送消息
                // {msg: $('#info').val(), receive: $('#receive').val()}
                Map<String, String> msgMap = JsonUtil.jsonStrToMap(message);
                System.out.println(msgMap);
                // 如果没有 receive 属性, 则向所有人发送
                if (Objects.nonNull(msgMap) && !StringUtils.hasText(msgMap.get("receive"))) {
                    String msgReturn = "{sendId:'" + session.getId() + "', info:'" + msgMap.get("msg") + "'}";
                    entry.getValue().getBasicRemote().sendText(msgReturn);
                } else {
                    // 向指定用户发送
                    if (Objects.nonNull(msgMap) && entry.getKey().equals(msgMap.get("receive"))) {
                        String msgReturn = "{sendId:'" + session.getId() + "', info:'" + msgMap.get("msg") + "'}";
                        entry.getValue().getBasicRemote().sendText(msgReturn);
                    }
                }
            }
        }
    }*/


    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("客服端 " + session.getId() + " 连接断开");
        // 用户下线: 从 map 移除对象, 因为查询时, 是用当前 sessionMap 中保存的所有在线用户与所有用户进行对比, 如果在 map 中表示该用户在线
        // 从 map 中移除, 则即可下线
        User curUser = this.getCurUser();
        sessionMap.remove(curUser);
        // 更新其他用户的列表, 而不是当前在线用户, 当前在线用户已经下线
        // todo 遍历 map 发送消息, 进行更新
        session.getBasicRemote().sendText(this.getUserList(curUser));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("客服端 " + session.getId() + " 连接出错");
        error.printStackTrace();
    }

}
