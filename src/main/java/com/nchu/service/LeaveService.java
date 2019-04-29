package com.nchu.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nchu.dto.LeaveResult;
import com.nchu.entity.Leave;

public interface LeaveService {
	Page<Leave> findByApplyid(String applyid,Pageable pageable);//通过申请者id查找所有相关的假条信息
	ArrayList<LeaveResult> castLeaveResult(List<Leave> list)throws ParseException;//把leave集合转为leaveResult集合
	void addLeave(Leave leave) throws ParseException;//添加假条
	void leaveCancel(Integer leaid)throws ParseException;//提前销假
}
