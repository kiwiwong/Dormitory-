package com.nchu.service;

import com.nchu.entity.User;

public interface UserService {

	User findByUid(String uid);
}
