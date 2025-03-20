package com.workmate.app.security.mapper;

import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.security.service.UserVO;

public interface UserMapper {
	public EmpVO getUserInfo(String id);
}
