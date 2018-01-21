package com.cdsxt.service;

import com.cdsxt.ro.ChatInfo;
import com.cdsxt.ro.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ChatService {

    /**
     * 获取登陆用户列表, 需要注明登陆状态
     *
     * @param userType
     * @param allOnLineUserList
     * @return
     */
    List<User> getUserListWithState(String userType, Set<User> allOnLineUserSet);

    /**
     * 保存消息到数据库中
     *
     * @param curUser  当前用户, 即发送人
     * @param messages 接收到的消息内容
     */
    void storeMessage(User curUser, Map<String, Object> messages);

    /**
     * 查询指定用户所有的未读消息数量
     *
     * @param userId 用户 id
     * @return 未读消息的数量
     */
    Integer countChatInfoNoRead(Integer userId);

    // 与每个用户的未读消息数量
    String countChatInfoWithEveryUser(User receiveUser);

    // 更新用户之间的消息状态为已读
    void updateMessageRead(User sendUser, User receiveUser);

    // 获取用户之间所有未读消息
    List<ChatInfo> getAllNoReadChatInfoTwoUser(User sendUser, User receiveUser);
}
