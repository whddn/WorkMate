package com.workmate.app.common;

import org.springframework.security.core.context.SecurityContextHolder;

import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.security.service.LoginUserVO;
import com.workmate.app.security.service.UserVO;

public class WhoAmI {
	public UserVO whoAmI() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(principal instanceof LoginUserVO) {
			LoginUserVO loginUserVO = (LoginUserVO) principal;
			return loginUserVO.getUserVO();
		}
		else {
			return new UserVO();
		}
	}
}
