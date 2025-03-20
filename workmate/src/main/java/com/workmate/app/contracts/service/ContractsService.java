package com.workmate.app.contracts.service;

import java.util.List;
import java.util.Map;

public interface ContractsService {
	// 전체조회
	public List<ContractsVO> findContractsList();
	// 단건조회
	public ContractsVO findContractsById(ContractsVO contractsVO);
	// 등록
	public int inputContracts(ContractsVO contractsVO);
	// 수정
	public int modifyContracts(ContractsVO contractsVO);
	// 삭제
	public Map<String, Object>dropContracts(String contrNo);
}
