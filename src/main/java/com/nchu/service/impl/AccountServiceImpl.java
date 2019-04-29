package com.nchu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nchu.dao.AccountDao;
import com.nchu.entity.Account;
import com.nchu.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	
	@Autowired
	private AccountDao accountDao;
	
	//通过账号查找Account对象
	@Override
	public Account findAccountByUid(String user_id) {
		// TODO Auto-generated method stub
		Account account = accountDao.queryAccountByUid(user_id);
		return account;
	}

	//通过账号id查找Account对象
	@Override
	public Account findAccountByAid(Integer user_id) {
		// TODO Auto-generated method stub
		return accountDao.queryAccountByAid(user_id);
	}
	

}
