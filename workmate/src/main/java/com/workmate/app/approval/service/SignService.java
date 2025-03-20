package com.workmate.app.approval.service;

import java.util.List;

import com.workmate.app.employee.service.EmpVO;

public interface SignService {
	public List<SignVO> findSignList(EmpVO empVO);
	public int inputSign(SignVO signVO);
	public int dropSign(SignVO signVO);
}
