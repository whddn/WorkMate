package com.workmate.app.approval.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface ApprFormService {
	public List<ApprFormVO> findFormList();
	public ApprFormVO findFormById(ApprFormVO apprFormVO);
}
