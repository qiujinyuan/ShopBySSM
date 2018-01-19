package com.cdsxt.service;

import com.cdsxt.ro.User;
import com.cdsxt.ro.UserAddress;

import java.util.List;

public interface UserService {

    // 根据用户名进行查询
    User selectOneByName(String username);

    // 根据用户名查询后台用户: 客服
    User selectBackUserByName(String username);

    // 根据用户 id 查询该用户的收货信息
    List<UserAddress> selectUserAddressById(Integer id);

    // 根据地址 id 查询收货信息
    UserAddress selectAddressById(Integer id);

}
