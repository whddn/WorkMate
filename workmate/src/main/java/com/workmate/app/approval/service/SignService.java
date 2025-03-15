package com.workmate.app.approval.service;

import java.util.List;

import com.workmate.app.employee.service.EmpVO;

public interface SignService {
	public List<SignVO> selectSignList(EmpVO empVO);
	public int insertSign(SignVO signVO);
	public int deleteSign(int signNo);
}
