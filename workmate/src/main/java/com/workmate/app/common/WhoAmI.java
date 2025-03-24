package com.workmate.app.common;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.workmate.app.employee.service.EmpService;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.security.service.LoginUserVO;

@Component
public class WhoAmI {
	private EmpService empService;
	
	public WhoAmI(EmpService empService) {
		this.empService = empService;
	}
	
	public EmpVO whoAmI() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(principal instanceof LoginUserVO) {
			LoginUserVO loginUserVO = (LoginUserVO) principal;
			EmpVO empVO = loginUserVO.getUserVO();
			return empService.findEmpByEmpNo(empVO);
		}
		else {
			return new EmpVO();
		}
	}
}
