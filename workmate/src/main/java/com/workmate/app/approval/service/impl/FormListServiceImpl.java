package com.workmate.app.approval.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ApprFormMapper;
import com.workmate.app.approval.service.ApprFormVO;
import com.workmate.app.approval.service.FormListService;

@Service
public class FormListServiceImpl implements FormListService {
	private ApprFormMapper apprFormMapper;
	
	@Autowired
	FormListServiceImpl(ApprFormMapper apprFormMapper) {
		this.apprFormMapper = apprFormMapper;
	}
	
	@Override
	public List<ApprFormVO> loadFormList() {
		// TODO Auto-generated method stub
		return apprFormMapper.selectApprFormList();
	}

}
