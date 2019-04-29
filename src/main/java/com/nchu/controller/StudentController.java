package com.nchu.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nchu.dto.LeaveResult;
import com.nchu.dto.Result;
import com.nchu.entity.Leave;
import com.nchu.entity.User;
import com.nchu.entity.Visitor;
import com.nchu.exception.LeaveException;
import com.nchu.service.LeaveService;
import com.nchu.service.UserService;
import com.nchu.service.VisitorService;

import utils.DetectMobile;
import utils.FileUpload;;

@Controller
public class StudentController {

	@Autowired
	private LeaveService leaveService;

	@Autowired
	private VisitorService visitorService;

	@Autowired
	private UserService userService;

	private Visitor findByVisitor;
	
	@RequiresRoles(value={"student"})
	@RequestMapping("/overview")
	public String overview(){
		
		return "overview";
	}

	@RequiresRoles(value = { "student" })
	@RequestMapping("/leave")
	public String leavePage(HttpServletRequest request,Model map, Integer page) throws ParseException {
		Subject subject = SecurityUtils.getSubject();
		String uid = subject.getPrincipal().toString();
		Page<Leave> pages = leaveService.findByApplyid(uid, PageRequest.of(page - 1, 6, Direction.DESC, "createTime"));// jpa当前页是按0的下标开始
		List<Leave> leaves = pages.getContent();
		ArrayList<LeaveResult> results = leaveService.castLeaveResult(leaves);
		map.addAttribute("results", results);
		map.addAttribute("pageTotal", pages.getTotalPages() == 0 ? 1 : pages.getTotalPages());
		map.addAttribute("page", page);
		if(DetectMobile.isMobile(request))
			return "m_leave";
		return "leave";
	}

	@RequiresRoles(value = { "student" })
	@RequestMapping("/leave/add")
	public String leaveAddPage() {
		return "leaveAdd";
	}

	@RequiresRoles(value = { "student" })
	@RequestMapping(value = "/leave/addLeave", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> addLeave(Leave leave) throws ParseException {
		Subject subject = SecurityUtils.getSubject();
		String uid = subject.getPrincipal().toString();
		leave.setApplyid(uid);
		try {
			leaveService.addLeave(leave);
		} catch (LeaveException e) {
			return new Result<Object>(true, e.getMessage());
			// }catch (Exception e) {
			// // TODO: handle exception
			// return new Result<Object>(true, "系统异常！");
		}
		return new Result<>(true, "申请成功！");
	}

	@RequiresRoles(value = { "student" })
	@RequestMapping("/visitor")
	public String visitor(Model map, Integer page) {
		Subject subject = SecurityUtils.getSubject();
		Page<Visitor> pages = visitorService.findByUid(subject.getPrincipal().toString(),
				PageRequest.of(page - 1, 6, Direction.DESC, "createTime"));
		map.addAttribute("results", pages.getContent());
		map.addAttribute("page", page);
		map.addAttribute("pageTotal", pages.getTotalPages() == 0 ? 1 : pages.getTotalPages());
		return "visitor";

	}

	@RequiresRoles(value = { "student" })
	@RequestMapping("leave/cancel")
	@ResponseBody
	public Result<Object> leaveCancel(Integer leaid) {
		try {
			leaveService.leaveCancel(leaid);
			return new Result<>(true);
		} catch (LeaveException e) {
			// TODO: handle exception
			return new Result<>(false, e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			return new Result<>(false, "系统错误!");
		}
	}

	@RequiresRoles(value = { "student" })
	@PostMapping("/visitor/add")
	public String visitorAdd(Visitor visitor, MultipartFile image) {
		System.out.println(visitor.getName());
		System.out.println(visitor.getSex());
		System.out.println(visitor.getPhone());
		System.out.println(visitor.getBeginTime());
		System.out.println(visitor.getType());
		Subject subject = SecurityUtils.getSubject();
		String uid = subject.getPrincipal().toString();
		User user = userService.findByUid(uid);
		System.out.println(user.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String faceid = FileUpload.imageUpload(image);
			visitor.setBuildingid(user.getBuildingid());
			visitor.setUid(uid);
			visitor.setFaceid(faceid);
			visitor.setCreateTime(sdf.format(new Date()));
			visitorService.save(visitor);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			return "forward:/visitor?page=1";
		}
		return "forward:/visitor?page=1";
	}

	@RequiresRoles(value = { "student" })
	@PostMapping("/visitor/edit/{id}")
	public String visitorEdit(@RequestParam("beginTime") String beginTime, @RequestParam("endTime") String endTime,
			@PathVariable("id") Integer visitorid) {
		Visitor visitor = visitorService.findByVisitor(visitorid);
		visitor.setBeginTime(beginTime);
		visitor.setEndTime(endTime);
		visitorService.save(visitor);
		return "forward:/visitor?page=1";

	}
}
