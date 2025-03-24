package com.workmate.app.mainscreen.mapper;

import java.util.List;

import com.workmate.app.mainscreen.service.ScheduleVO;

public interface ScheduleMapper {
	public List<ScheduleVO> selectScheduleList(ScheduleVO scheduleVO);
	public int insertSchedule(ScheduleVO scheduleVO);
	public int updateSchedule(ScheduleVO scheduleVO);
	public int deleteSchedule(ScheduleVO scheduleVO);
}
