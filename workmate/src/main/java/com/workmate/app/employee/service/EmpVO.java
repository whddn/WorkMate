package com.workmate.app.employee.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class EmpVO {
	private Integer userNo;          // 사원번호
	private String userId;			// 아이디
	private String userPwd;			// 비밀번호
	private String userMail;			// 메일 
	private String userPosition;		// 직급 
	// java.util.Date : yyyy/MM/dd
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date hireDate;			// 입사일자
	private String userName; 		// 사원 이름
	//@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date resignDate;			// 퇴사일자
	private String statusUser; 		// 재직 여부??? 
	//@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date userBirth; 			// 생년월일
	private String address;			// 주소
	private String userPhone;		// 전화번호
	private String teamNo; 			// 팀 번호
	private String userIp;			// IP 주소
	private String commonIp;			// 회사 IP
	
	private String departmentName; 	// 부서명
	private String teamName; 		// 팀 이름
	
	// 팀 
	private String departmentId;		// 부서번호
	private int leaderNo;			// 부서장번호 (사원번호)
	private String teamPermission;	// 팀 권한 
	
	private String code;			// 코드
	private String codeName;			// 직급 이름
}
