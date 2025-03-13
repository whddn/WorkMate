package com.workmate.app.approval.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface FormListService {
	public List<ApprFormVO> load() {
		// TODO Auto-generated method stub
		return empMapper.selectEmpList();
	}
}
