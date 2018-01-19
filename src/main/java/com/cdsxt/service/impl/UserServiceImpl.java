package com.cdsxt.service.impl;

import com.cdsxt.dao.UserDao;
import com.cdsxt.ro.User;
import com.cdsxt.ro.UserAddress;
import com.cdsxt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User selectOneByName(String username) {
        return userDao.selectOneByName(username);
    }

    @Override
    public User selectBackUserByName(String username) {
        return userDao.selectBackUserByName(username);
    }

    @Override
    public List<UserAddress> selectUserAddressById(Integer id) {
        return userDao.selectUserAddressById(id);
    }

    @Override
    public UserAddress selectAddressById(Integer id) {
        return userDao.selectAddressById(id);
    }


}
