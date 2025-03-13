package com.workmate.app.employee.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class EmpVO {
	public Integer userNo;          // 사원번호
	public String userId;			// 아이디
	public String userPwd;			// 비밀번호
	public String userMail;			// 메일 
	public String userPosition;		// 직급 
	// java.util.Date : yyyy/MM/dd
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date hireDate;			// 입사일자
	public String userName; 		// 사원 이름
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date resignDate;			// 퇴사일자
	public String statusUser; 		// 재직 여부??? 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date userBirth; 			// 생년월일
	public String address;			// 주소
	public String userPhone;		// 전화번호
	public String teamNo; 			// 팀 번호
	public String userIp;			// IP 주소
	public String commonIp;			// 회사 IP
	
	public String departmentName; 	// 부서명
	public String teamName; 		// 팀 이름
	
	// 팀 
	public String departmentId;		// 부서번호
	public int leaderNo;			// 부서장번호 (사원번호)
	public String teamPermission;	// 팀 권한 
}
