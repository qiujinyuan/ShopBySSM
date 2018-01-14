package com.cdsxt.service.impl;

import com.cdsxt.dao.UserFrontDao;
import com.cdsxt.ro.UserAddress;
import com.cdsxt.ro.UserFront;
import com.cdsxt.service.UserFrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserFrontServiceImpl implements UserFrontService {

    @Autowired
    private UserFrontDao userFrontDao;

    @Override
    public UserFront selectOneByName(String username) {
        return userFrontDao.selectOneByName(username);
    }

    @Override
    public List<UserAddress> selectUserAddressById(Integer id) {
        return userFrontDao.selectUserAddressById(id);
    }

    @Override
    public UserAddress selectAddressById(Integer id) {
        return userFrontDao.selectAddressById(id);
    }


}
