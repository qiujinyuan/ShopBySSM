package com.cdsxt.mangodb;

import com.cdsxt.ro.ChatInfo;

import java.util.List;

public interface ChatInfoDao {


    /**
     * 查询用户之间的聊天消息
     *
     * @param sendUserId    发信人 id
     * @param receiveUserId 收信人 id
     * @return 所有聊天记录, 且按照时间倒序排列
     */
    List<ChatInfo> queryChatInfoTwoUser(Integer sendUserId, Integer receiveUserId);


    /**
     * 查询指定用户所有的未读消息
     *
     * @param userId 用户 id
     * @return 未读消息
     */
    List<ChatInfo> queryChatInfoNoRead(Integer userId);

    /**
     * 查询指定用户所有的未读消息数量
     *
     * @param userId 用户 id
     * @return 未读消息的数量
     */
    Integer countChatInfoNoRead(Integer userId);

    /**
     * 查询用户之间的所有未读消息
     *
     * @param sendUserId    发信人 id
     * @param receiveUserId 收信人 id
     * @return 所有未读的聊天记录, 且按照时间倒序排列
     */
    List<ChatInfo> queryChatInfoTwoUserNoRead(Integer sendUserId, Integer receiveUserId);

    /**
     * 查询用户之间的所有未读消息的数量
     *
     * @param sendUserId    发信人 id
     * @param receiveUserId 收信人 id
     * @return 所有未读的聊天记录的数量
     */
    Integer countChatInfoTwoUserNoRead(Integer sendUserId, Integer receiveUserId);


    /**
     * 保存一条聊天记录
     *
     * @param chatInfo 聊天记录
     */
    void saveChatInfo(ChatInfo chatInfo);

    /**
     * 将指定两个用户之间的所有消息状态改为已读
     *
     * @param sendUserId
     * @param receiveUserId
     */
    void updateReadChatInfoTwoUser(Integer sendUserId, Integer receiveUserId);
}