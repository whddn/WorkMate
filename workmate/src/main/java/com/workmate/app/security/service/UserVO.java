package com.workmate.app.security.service;

import java.util.Date;

import lombok.Data;

@Data
public class UserVO {
	private String userNo; //사원번호
	private String userId; // 사원아이디
	private String userPwd; // 사원비밀번호
	private String userMail; //사원메일
	private String userPosition; //직급
	private Date hireDate; //입사일
	private String userName; //사원이름
	private Date resignDate; //퇴사일
	private String statusUser; //재직여부
	private Date userBirth; //생년월일
	private String address; //주소
	private String userPhone; //번호
	private String teamNo; //팀번호
	private String userIp; //아이피
}
