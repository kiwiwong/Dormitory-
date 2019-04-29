package com.nchu.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nchu.entity.Major;

public interface MajorDao extends JpaRepository<Major, Integer> {
	
	Major queryByClassid(String classid);
}
