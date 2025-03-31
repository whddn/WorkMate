package com.workmate.app.employee.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EvaluStatusScheduler {
	private final EmpService empService;
	// 평가 마감일자가 지나면 자동으로 평가 종료 상태가 됨
	 @Scheduled(cron = "0 0 0 * * ?")
	    public void updateEvaluStatusByEndDate() {
	        int updated = empService.autoUpdateStatusByDate();
	        System.out.println("🕛 자동 상태 변경 실행됨 - 변경된 행 수: " + updated);
	    }
}
