package com.cdsxt.config;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;


/**
 * 获取 HttpSession 的配置类
 */
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec,
                                HandshakeRequest request,
                                HandshakeResponse response) {
        // 取出httpsession
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        //放入websocket配置数据中
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}