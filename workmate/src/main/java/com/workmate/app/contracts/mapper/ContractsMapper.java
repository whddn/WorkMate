package com.workmate.app.contracts.mapper;

import java.util.List;

import com.workmate.app.contracts.service.ContractsVO;

public interface ContractsMapper {
	// 전자계약 전체조회
	public List<ContractsVO> selectContractsList();
	// 단건조회
	public ContractsVO selectContractsById(ContractsVO contractsVO);
	// 등록
	public int insertContracts(ContractsVO contractsVO);
	// 수정
	public int updateContracts(ContractsVO contractsVO);
	// 삭제
	public int deleteContracts(String contrNo);
}
