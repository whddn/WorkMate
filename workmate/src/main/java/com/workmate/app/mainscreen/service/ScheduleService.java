package com.workmate.app.mainscreen.service;

import java.util.List;

public interface ScheduleService {
	public List<ScheduleVO> findScheduleList(ScheduleVO scheduleVO);
	public int inputSchedule(ScheduleVO scheduleVO);
	public int modifySchedule(ScheduleVO scheduleVO);
	public int dropSchedule(ScheduleVO scheduleVO);
}
