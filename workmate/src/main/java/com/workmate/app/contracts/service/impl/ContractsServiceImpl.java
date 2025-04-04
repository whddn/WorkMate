package com.workmate.app.contracts.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ApprFormMapper;
import com.workmate.app.approval.service.ApprFormVO;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.contracts.mapper.ContractsMapper;
import com.workmate.app.contracts.service.ContractsService;
import com.workmate.app.contracts.service.ContractsVO;

@Service

public class ContractsServiceImpl implements ContractsService{
	
	private ContractsMapper contractsMapper;
	private ApprFormMapper apprFormMapper;
	
	@Autowired
	ContractsServiceImpl(ApprFormMapper apprFormMapper, ContractsMapper contractsMapper) {
		this.apprFormMapper = apprFormMapper;
		this.contractsMapper = contractsMapper;
	}

	@Override
	public List<ContractsVO> findContractsList() {
		return contractsMapper.selectContractsList();
	}

	@Override
	public ContractsVO findContractsById(ContractsVO contractsVO) {
		return contractsMapper.selectContractsById(contractsVO);
	}

	@Override
	public int inputContracts(ContractsVO contractsVO) {
		return contractsMapper.insertContracts(contractsVO);
	}

	@Override
	public int modifyContracts(ContractsVO contractsVO) {
		return contractsMapper.updateContracts(contractsVO);
	}

	@Override
	public int dropContracts(String contrNo) {
		return contractsMapper.deleteContracts(contrNo);
	}



//	
	
}
