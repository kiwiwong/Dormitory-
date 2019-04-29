package com.nchu;

import java.util.Date;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import com.nchu.dao.LeaveDao;
import com.nchu.entity.Account;
import com.nchu.entity.Role;
import com.nchu.entity.Visitor;
import com.nchu.service.AccountRoleService;
import com.nchu.service.AccountService;
import com.nchu.service.RolePermissionService;
import com.nchu.service.VisitorService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DormitoryApplicationTests {

	@Autowired
	private RolePermissionService rpService;
	
	@Autowired
	private AccountRoleService arService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private LeaveDao leaveDao;
	
	@Autowired
	private VisitorService visitorService;
	
	
	@Test
	public void contextLoads() {
		
	}

	@Test
	public void test01(){
		Account account=accountService.findAccountByAid(1);
		System.out.println(account.getStatus());
	}
	
	@Test
	public void encryption(){
		
		//Account account = accountService.findAccountByUid("root");
		ByteSource salt = ByteSource.Util.bytes("15046221Nchu031234");
		Object md = new SimpleHash("MD5","Nchu031234",salt,1024);
		System.out.println(md.toString());
	}
	
	@Test
	public void leaveDao(){
		System.out.println(leaveDao.queryByApplyid("15046220",new Sort(Direction.DESC, "createTime")).get(0).toString());
	}
	
	@Test
	public void visitorSave(){
		Visitor visitor = new Visitor();
		Date date = new Date();
		visitor.setBeginTime(date.toString());
		visitor.setEndTime(date.toString());
		visitor.setCreateTime(date.toString());
		visitor.setFaceid("12346");
		visitor.setName("123");
		visitor.setPhone("13123");
		visitor.setSex("男");
		visitor.setUid("131231");
		visitorService.save(visitor);
	}
}
