package com.workmate.app.approval.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.SignMapper;
import com.workmate.app.approval.service.SignService;
import com.workmate.app.approval.service.SignVO;
import com.workmate.app.employee.service.EmpVO;

@Service
public class SignServiceImpl implements SignService {
	private SignMapper signMapper;
	
	@Autowired
	SignServiceImpl(SignMapper signMapper) {
		this.signMapper = signMapper;
	}
	
	@Override
	public List<SignVO> selectSignList(EmpVO empVO) {
		// TODO Auto-generated method stub
		return signMapper.selectSignList(empVO);
	}

	@Override
	public int insertSign(SignVO signVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteSign(int signNo) {
		// TODO Auto-generated method stub
		return 0;
	}

}
