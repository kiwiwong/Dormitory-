package com.nchu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nchu.dao.UserDao;
import com.nchu.entity.User;
import com.nchu.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public User findByUid(String uid) {
		// TODO Auto-generated method stub
		return userDao.queryByUid(uid);
	}

}
