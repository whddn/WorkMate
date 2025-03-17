package com.workmate.app.approval.mapper;

import java.util.List;

import com.workmate.app.approval.service.SignVO;
import com.workmate.app.employee.service.EmpVO;

public interface SignMapper {
	public List<SignVO> selectSignList(EmpVO empVO);
	public int insertSign(SignVO signVO);
	public int deleteSign(int signNo);
}
