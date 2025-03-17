package com.workmate.app.employee.service;

import lombok.Data;

@Data
public class TeamVO {
	private String teamNo;			// 팀 번호
	private String teamName;		// 팀 이름
	private int leaderNo;			// 팀장 번호 (사원번호)
	private String teamPermission; 	// 팀 권한 
}
