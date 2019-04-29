package com.nchu.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nchu.entity.Access;

public interface AccessDao extends JpaRepository<Access,Integer> {

}
