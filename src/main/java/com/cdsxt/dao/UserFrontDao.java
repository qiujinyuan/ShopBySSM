package com.cdsxt.dao;

import com.cdsxt.ro.UserAddress;
import com.cdsxt.ro.UserFront;

import java.util.List;

/**
 * UserFront bean 数据库访问层
 */

public interface UserFrontDao {

    // 根据用户名进行查询
    UserFront selectOneByName(String username);

    // 根据用户 id 查询该用户的收货信息
    List<UserAddress> selectUserAddressById(Integer id);

    // 根据地址 id 查询收货信息
    UserAddress selectAddressById(Integer id);
}
