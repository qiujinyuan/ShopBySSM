package com.cdsxt.service.impl;

import com.cdsxt.dao.UserDao;
import com.cdsxt.mangodb.ChatInfoDao;
import com.cdsxt.ro.ChatInfo;
import com.cdsxt.ro.User;
import com.cdsxt.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ChatInfoDao chatInfoDao;

    @Override
    public List<User> getUserListWithState(String userType, Set<User> allOnLineUserSet) {
        List<User> userList;
        if ("backUser".equals(userType)) {
            // 查询所有客服, 同时设置用户类型
            userList = this.setUserType(userType, this.userDao.selectAllBackUser());
        } else if ("frontUser".equals(userType)) {
            // 查询所有用户
            userList = this.setUserType(userType, this.userDao.selectAllFrontUser());
        } else {
            return null;
        }
        return this.compareAndSetOnline(userList, allOnLineUserSet);
    }

    @Override
    public void storeMessage(User curUser, Map<String, Object> messages) {
        // 构建 ChatInfo 对象
        ChatInfo newInfo = new ChatInfo();
        // 未读消息
        newInfo.setIsRead(false);
        newInfo.setMsgContent((String) messages.get("msgContent"));
        newInfo.setReceiveUserId((Integer) messages.get("receiveUserId"));
        newInfo.setReceiveUserName((String) messages.get("receiveUserName"));
        newInfo.setReceiveUserType((String) messages.get("receiveUserType"));
        // 保存当前时间
        newInfo.setSendDate(new Date());
        newInfo.setSendUserId(curUser.getUid());
        newInfo.setSendUserName(curUser.getName());
        newInfo.setSendUserType(curUser.getType());
        // 调用保存方法
        this.chatInfoDao.saveChatInfo(newInfo);
    }

    @Override
    public Integer countChatInfoNoRead(Integer userId) {
        return this.chatInfoDao.countChatInfoNoRead(userId);
    }

    @Override
    public String countChatInfoWithEveryUser(User receiveUser) {
        StringBuilder userListCount = new StringBuilder();
        userListCount.append("{");
        // 如果是用户, 则查询所有与所有客服的未读消息数量
        if ("frontUser".equals(receiveUser.getType())) {
            for (User user : this.userDao.selectAllBackUser()) {
                Integer count = this.chatInfoDao.countChatInfoTwoUserNoRead(user.getUid(), receiveUser.getUid());
                if (count > 0) {
                    // 有未读消息, 添加
                    userListCount.append("\"").append(user.getName()).append("\"").append(":").append(count).append(",");
                }
            }
        } else if ("backUser".equals(receiveUser.getType())) {
            // 如果是客服, 则查询与所有用户的未读消息数量
            for (User user : this.userDao.selectAllFrontUser()) {
                Integer count = this.chatInfoDao.countChatInfoTwoUserNoRead(user.getUid(), receiveUser.getUid());
                if (count > 0) {
                    // 有未读消息, 添加
                    userListCount.append("\"").append(user.getName()).append("\"").append(":").append(count).append(",");
                }
            }
        }
        String result = userListCount.substring(0, userListCount.length() - 1);

        return result + "}";
    }

    @Override
    public void updateMessageRead(User sendUser, User receiveUser) {
        this.chatInfoDao.updateReadChatInfoTwoUser(sendUser.getUid(), receiveUser.getUid());
    }

    @Override
    public List<ChatInfo> getAllNoReadChatInfoTwoUser(User sendUser, User receiveUser) {
        return this.chatInfoDao.queryChatInfoTwoUserNoRead(sendUser.getUid(), receiveUser.getUid());
    }

    // 获取所有消息
    @Override
    public List<ChatInfo> getAllMsgChatInfoTwoUser(User sendUser, User receiveUser) {
        return this.chatInfoDao.queryChatInfoTwoUser(sendUser.getUid(), receiveUser.getUid());
    }

    // 因为使用 mybatis 查询出来的 User 对象, 默认没有用户类型, 所有需要进行设置
    private List<User> setUserType(String userType, List<User> userList) {
        for (User user : userList) {
            user.setType(userType);
        }
        return userList;
    }

    // 与所有登陆用户对比, 设置在线状态
    private List<User> compareAndSetOnline(List<User> userList, Set<User> allOnLineUserSet) {
        for (User user : userList) {
            if (allOnLineUserSet.contains(user)) {
                // 在线
                user.setOnline(true);
            } else {
                // 不在线
                user.setOnline(false);
            }
        }
        return userList;
    }


}
