package com.nchu.service;

import com.nchu.entity.Account;

public interface AccountService {
		
	Account findAccountByUid(String user_id);//通过账号查找Account对象
	Account findAccountByAid(Integer user_id);//通过账号id查找Account对象
}
