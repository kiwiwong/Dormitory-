package com.nchu.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nchu.dao.LeaveDao;
import com.nchu.dao.MajorDao;
import com.nchu.dao.UserDao;
import com.nchu.dto.LeaveResult;
import com.nchu.entity.Leave;
import com.nchu.entity.User;
import com.nchu.exception.LeaveException;
import com.nchu.service.LeaveService;

@Service
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	private LeaveDao leaveDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private MajorDao majorDao;

	@Override
	public Page<Leave> findByApplyid(String applyid, Pageable pageable) {
		// TODO Auto-generated method stub
		return leaveDao.queryByApplyid(applyid, pageable);
	}

	@Override
	public ArrayList<LeaveResult> castLeaveResult(List<Leave> list) throws ParseException {
		// TODO Auto-generated method stub
		LeaveResult leaveResult = new LeaveResult();
		ArrayList<LeaveResult> result = new ArrayList<>();
		for (Leave leave : list) {
			User applyUser = userDao.queryByUid(leave.getApplyid());
			User treatUser = userDao.queryByUid(leave.getTreatid());
			leaveResult.setLeaid(leave.getLeaid());
			leaveResult.setApplyName(applyUser.getName());
			leaveResult.setTreatName(treatUser.getName());
			leaveResult.setBeginTime(leave.getBeginTime());
			leaveResult.setCancelTime(leave.getCancelTime());
			leaveResult.setEndTime(leave.getEndTime());
			leaveResult.setIscancel(leave.getIscancel());
			leaveResult.setReason(leave.getReason());
			leaveResult.setStatus(leave.getStatus());
			leaveResult.setOperate(isOnLeave(leave) && leave.getStatus() == 1 && leave.getIscancel() == 0);
			result.add(leaveResult);
		}
		return result;
	}

	@Override
	public void addLeave(Leave leave) throws ParseException {
		// TODO Auto-generated method stub
		// 判断最近假条结束日期是否位于当前日期之前
		// 判断是否有请假记录
		if (isOnLeave(leave)) {
			throw new LeaveException("处于假期期间不能申请！");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date creatTime = new Date();
		String creatTimeStr = sdf.format(creatTime);
		leave.setCreateTime(creatTimeStr);
		User user = userDao.queryByUid(leave.getApplyid());
		String treatid = majorDao.queryByClassid(user.getClassid()).getUid();
		leave.setTreatid(treatid);
		leave.setStatus(0);
		leave.setIscancel(0);
		leaveDao.save(leave);
		return;
	}

	// 判断是否处于请假期间
	public boolean isOnLeave(Leave leave) throws ParseException {
		String applyUser = leave.getApplyid();
		Sort sort = new Sort(Direction.DESC, "endTime");
		Pageable pageable = PageRequest.of(0, 1, sort);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Page<Leave> list = leaveDao.queryByApplyid(applyUser, pageable);
		if (list.isEmpty()) {
			return false;
		}
		Date date = sdf.parse(list.getContent().get(0).getEndTime());
		Date now = new Date();
		Calendar before = Calendar.getInstance();
		Calendar after = Calendar.getInstance();
		before.setTime(date);
		after.setTime(now);
		if (after.after(before)) {
			return false;
		}
		return true;
	}

	@Override
	public void leaveCancel(Integer leaid) throws ParseException {
		// TODO Auto-generated method stub
		// 通过leaid获取对应的leave
		Leave leave = leaveDao.queryByLeaid(leaid);
		// 判断假条状态
		if (leave.getStatus() != 1)
			throw new LeaveException("假条没有通过审核不能提前销假！");
		// 判断假条结束日期
		if (!isOnLeave(leave))
			throw new LeaveException("假条期限已过不能销假！");
		// 设置假条是否销假状态
		if(leave.getIscancel()==1)
			throw new LeaveException("假条已经提前销假!");
		leave.setIscancel(1);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		leave.setCancelTime(sdf.format(date).toString());
		leaveDao.save(leave);
	}

}
