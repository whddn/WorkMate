package com.workmate.app.attendance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.attendance.mapper.AttendMapper;
import com.workmate.app.attendance.service.AttendanceService;
import com.workmate.app.attendance.service.WorkVO;

@Service
public class AttendanceServiceImpl implements AttendanceService {
	
	private AttendMapper attendMapper;
	
	@Autowired
	AttendanceServiceImpl(AttendMapper attendMapper){
		this.attendMapper = attendMapper;
	}
	
	@Override
	public List<WorkVO> findAllWork() {
		return attendMapper.selectWorkList();
	}

}
