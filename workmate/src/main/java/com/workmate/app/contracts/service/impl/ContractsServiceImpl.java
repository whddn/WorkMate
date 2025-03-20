package com.workmate.app.contracts.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.contracts.mapper.ContractsMapper;
import com.workmate.app.contracts.service.ContractsService;
import com.workmate.app.contracts.service.ContractsVO;

@Service

public class ContractsServiceImpl implements ContractsService{
	@Autowired
	private ContractsMapper contractsMapper;

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
		return 0;
	}

	@Override
	public int modifyContracts(ContractsVO contractsVO) {
		return 0;
	}

	@Override
	public Map<String, Object> dropContracts(String contrNo) {
		return null;
	} 
	
	
}
