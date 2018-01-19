package com.cdsxt.service;

import com.cdsxt.ro.User;

import java.util.List;
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
}
