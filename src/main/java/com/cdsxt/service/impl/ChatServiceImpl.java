package com.cdsxt.service.impl;

import com.cdsxt.dao.UserDao;
import com.cdsxt.ro.User;
import com.cdsxt.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    @Autowired
    private UserDao userDao;

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
