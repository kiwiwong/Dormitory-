package com.nchu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.nchu.entity.Account;
import com.nchu.service.AccountService;

import utils.DetectMobile;

@Controller
public class IndexController {
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping("/login")
	public String login(){
		
		return "login";
	}
	
	@RequestMapping("/")
	public String toIndex(HttpServletRequest request){
		if(DetectMobile.isMobile(request))
			return "m_index";
		return "login";
	}
	
	@RequestMapping(value="/toLogin",method=RequestMethod.POST)
	public String toLogin(String username,String password,String cat,ModelMap map,HttpServletRequest request){
		HttpSession session =request.getSession();
		Account account = accountService.findAccountByUid(username);
		
		if(account==null){
			map.put("msg", "账号不存在！");
			return "login";
		}
		if(!account.getType().equals(cat)){
			map.put("msg", "账号类型错误！");
			return "login";
		}
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
            subject.login(token);
            session.setAttribute("image", account.getImage()+".png");
        } catch (AuthenticationException e) {
            token.clear();
            map.put("msg", "账号或密码错误！");
            return "login";
        }
//		if(account.getType()=="admin"){
//			return "admin";
//		}
		if(DetectMobile.isMobile(request))
			return "m_index";
		return "index";
		
	}
	
	//@RequiresPermissions (value={"stu_read"})
	@RequiresRoles(value={"student"})
	@RequestMapping("/student")
	public String student(){
		return "stu";
	}
	
	@RequiresPermissions (value={"stu_read"})
	@RequestMapping("/stu_read")
	public String student2(){
		return "stu";
	}
	
	@RequiresPermissions (value={"teacher"})
	@RequestMapping("/teacher")
	public String teacher(){
		return "tea";
	}
	
	@RequiresRoles(value={"admin"})
	@RequestMapping("/admin")
	public String admin(){
		return "admin";
	}
}
