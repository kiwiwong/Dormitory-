package com.nchu.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nchu.entity.User;

public interface UserDao extends JpaRepository<User, Integer> {
	
	User queryByUid(String uid);
}
