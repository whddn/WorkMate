package com.workmate.app.mainscreen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.mainscreen.mapper.ScheduleMapper;
import com.workmate.app.mainscreen.service.ScheduleService;
import com.workmate.app.mainscreen.service.ScheduleVO;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	private ScheduleMapper scheduleMapper;
	
	@Autowired
	public ScheduleServiceImpl(ScheduleMapper scheduleMapper) {
		this.scheduleMapper = scheduleMapper;
	}
	
	@Override
	public List<ScheduleVO> findScheduleList(ScheduleVO scheduleVO) {
		// TODO Auto-generated method stub
		return scheduleMapper.selectScheduleList(scheduleVO);
	}

	@Override
	public int inputSchedule(ScheduleVO scheduleVO) {
		// TODO Auto-generated method stub
		return scheduleMapper.insertSchedule(scheduleVO);
	}
	
	@Override
	public int modifySchedule(ScheduleVO scheduleVO) {
		// TODO Auto-generated method stub
		return scheduleMapper.updateSchedule(scheduleVO);
	}

	@Override
	public int dropSchedule(ScheduleVO scheduleVO) {
		// TODO Auto-generated method stub
		return scheduleMapper.deleteSchedule(scheduleVO);
	}
}
