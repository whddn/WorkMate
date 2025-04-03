package com.workmate.app.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workmate.app.finance.mapper.FinanceMapper;
import com.workmate.app.finance.service.CorcardVO;
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
	@Transactional
	@Override
	public int inputReportPage(ReportVO reportVO) {
	    int result = 0;

	    // 1. 리포트 등록
	    int reportInsert = financeMapper.insertReportOne(reportVO);

	    if (reportInsert > 0) {
	        Integer reportNo = reportVO.getReportNo();
	        List<ReportVO> transList = reportVO.getTransHistoryList();
	        
	        int totalDeposit = 0;
	        int totalWithdraw = 0;
	        // 2. 거래 내역 등록
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
	                // 🧮 합계 계산
	                if (trans.getDeposit() != null) totalDeposit += trans.getDeposit();
	                if (trans.getWithdrawal() != null) totalWithdraw += trans.getWithdrawal();
	            }
	            int totalBalance = totalDeposit - totalWithdraw;
	            // 3. 리포트에 합계 금액 update
		            ReportVO updateVO = new ReportVO();
		            updateVO.setReportNo(reportNo);
		            updateVO.setTotalDep(totalDeposit);
		            updateVO.setTotalDrawal(totalWithdraw);
		            updateVO.setTotalBal(totalBalance);
		            result += financeMapper.updateReportTotalAmounts(updateVO);
	        }
	    }
	    return result;
	    
	}
	
	// 리포트에 합계 추가
	@Override
	public int modifyReportTotalAmounts(ReportVO reportVO) {
	    return financeMapper.updateReportTotalAmounts(reportVO);
	}
	// 입출금 리포트 수정 기능
	@Override
	public int modifyReportPage(ReportVO reportVO) {
		  int result = 0;

		    // 1. 리포트 수정
		    int reportUpdate = financeMapper.updateReportOne(reportVO);
		    System.out.println("리포트 수정 결과: " + reportUpdate);
		    if (reportUpdate > 0) {
		        Integer reportNo = reportVO.getReportNo();
		        List<ReportVO> transList = reportVO.getTransHistoryList();
		        
		        System.out.println("전체 거래 내역 수: " + (transList == null ? "null" : transList.size()));
		        if (transList != null && !transList.isEmpty()) {
		        	
		        	for (ReportVO trans : transList) {
		        	    trans.setReportNo(reportNo);
		        	    trans.setReportTitle(reportVO.getReportTitle());

		        	    System.out.println("거래내역 처리 중 → transId: " + trans.getTransId());

		        	    if (trans.getTransId() != null) {
		        	        int updateResult = financeMapper.updateTransHistory(trans);
		        	        System.out.println("UPDATE 결과: " + updateResult + "건 | transId: " + trans.getTransId());

		        	        if (updateResult == 0) {
		        	            System.out.println("UPDATE 실패 / 변경된 데이터 없음 → transId: " + trans.getTransId());
		        	        }

		        	        result += updateResult;
		        	    } else {
		        	        int insertResult = financeMapper.insertReportTransOne(trans);
		        	        result += insertResult;
		        	    }
		        	}
		        }

		    }

		    return result;
	}
	
	
	// 법인카드 등록
	@Override
	public void inputCorCard(CorcardVO card) throws Exception {
		financeMapper.insertCorCard(card);
	}
	// 법인카드 리스트 전체 조회
	@Override
	public List<CorcardVO> findCorcardList(CorcardVO corcardVO) {
		return financeMapper.selectCorCardList(corcardVO);
	}
	
	// 법인 카드 상세
	@Override
	public CorcardVO findCorcardById(CorcardVO corcardVO) {
		return financeMapper.selectCorcardById(corcardVO);
	}
	
}
