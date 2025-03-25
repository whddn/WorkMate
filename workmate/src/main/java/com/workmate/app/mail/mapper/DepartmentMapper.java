package com.workmate.app.mail.mapper;

import java.util.List;

import com.workmate.app.employee.service.DepartmentVO;

public interface DepartmentMapper {
	List<DepartmentVO> selectDepartmentList();
}
