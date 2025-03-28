package com.workmate.app.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.finance.mapper.FinanceMapper;
import com.workmate.app.finance.service.FinanceService;
import com.workmate.app.finance.service.ReportVO;

@Service
public class FinanceServiceImpl implements FinanceService {
	private FinanceMapper financeMapper;

	@Autowired
	public FinanceServiceImpl(FinanceMapper financeMapper) {
		this.financeMapper = financeMapper;
	}
	
	// 입출금 리포트 조회
	@Override
	public List<ReportVO> findReportList(ReportVO reportVO) {
		return financeMapper.selectReportList(reportVO);
	}
	


	// 입출금 리포트 - 한 보고서에 대한 모든 입출금 리스트
	@Override
	public List<ReportVO> findTransList(ReportVO reportVO) {
		return financeMapper.selectTransList(reportVO);
	}
	
	// 입출금 리포트 등록 
	@Override
	public int inputReportPage(ReportVO reportVO) {
	    int result = 0;

	    // 1. 리포트 등록
	    int reportInsert = financeMapper.insertReportOne(reportVO);

	    if (reportInsert > 0) {
	        Integer reportNo = reportVO.getReportNo();
	        List<ReportVO> transList = reportVO.getTransHistoryList();

	        if (transList != null && !transList.isEmpty()) {
	            for (ReportVO trans : transList) {
	                ReportVO transInsertVO = new ReportVO(); // 새 객체 생성

	                // 거래 관련 값만 복사
	                transInsertVO.setReportNo(reportNo);
	                transInsertVO.setTransDate(trans.getTransDate());
	                transInsertVO.setTransType(trans.getTransType());
	                transInsertVO.setWithdrawal(trans.getWithdrawal());
	                transInsertVO.setDeposit(trans.getDeposit());
	                transInsertVO.setBalance(trans.getBalance());
	                transInsertVO.setPurposeUse(trans.getPurposeUse());

	                result += financeMapper.insertReportTransOne(transInsertVO);
	            }
	        }
	    }

	    return result;
	}

	// 입출금 리포트 수정 기능
	@Override
	public int modifyReportPage(ReportVO reportVO) {
		  int result = 0;

		    // 1. 리포트 수정
		    int reportUpdate = financeMapper.updateReportOne(reportVO);

		    if (reportUpdate > 0) {
		        Integer reportNo = reportVO.getReportNo();

		        // 2. 기존 거래 내역 삭제
		       // financeMapper.deleteTransHistoryByReportNo(reportNo);

		        // 3. 새로운 거래 내역 insert
		        List<ReportVO> transList = reportVO.getTransHistoryList();

		        if (transList != null && !transList.isEmpty()) {
		            for (ReportVO trans : transList) {
		                ReportVO transInsertVO = new ReportVO();

		                transInsertVO.setReportNo(reportNo);
		                transInsertVO.setTransDate(trans.getTransDate());
		                transInsertVO.setTransType(trans.getTransType());
		                transInsertVO.setWithdrawal(trans.getWithdrawal());
		                transInsertVO.setDeposit(trans.getDeposit());
		                transInsertVO.setBalance(trans.getBalance());
		                transInsertVO.setPurposeUse(trans.getPurposeUse());

		                result += financeMapper.insertReportTransOne(transInsertVO);
		            }
		        }
		    }

		    return result;
	}
	
}
