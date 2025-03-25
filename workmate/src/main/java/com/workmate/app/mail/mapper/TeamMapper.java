package com.workmate.app.mail.mapper;

import java.util.List;

import com.workmate.app.employee.service.TeamVO;

public interface TeamMapper {
	List<TeamVO> selectTeamListByDepartment(int departmentId);
}
