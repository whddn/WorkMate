package com.workmate.app.employee.service;


import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.workmate.app.employee.mapper.EmpMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EvaluStatusScheduler {
	private final EmpService empService;
    private final EmpMapper empMapper; 
	// 평가 마감일자가 지나면 자동으로 평가 종료 상태가 됨
	 @Scheduled(cron = "0 0 0 * * ?")
	    public void updateEvaluStatusByEndDate() {
	        int updated = empService.autoUpdateStatusByDate();
	        System.out.println("🕛 자동 상태 변경 실행됨 - 변경된 행 수: " + updated);
	    }
	 
	 @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
	    public void autoUpdateEvaluToInProgress() {
	        LocalDate today = LocalDate.now();
	        int updated = empMapper.updateStatusToInProgress(today);
	        System.out.println("🔄 평가 자동 상태 변경 실행 - 진행 중으로 바뀐 평가 수: " + updated);
	    }
	 
}
