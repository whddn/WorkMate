package com.workmate.app.document.service;

import java.util.Date;

import lombok.Data;

@Data
public class DocVO {
	
	private int documentNo; //자료번호	
	private String fileName; //파일명
	private String fileDesc; //파일명
	private Long fileSize; //파일크기
	private String attachment; //첨부파일
	private String	extenstion; //확장자
	private Date fileRegDate; //등록날짜
	private String	fileTag; //태그
	private Integer userNo; //사원번호
	private String TeamNo; //팀번호
	private String userId; //사원이이디
	private Integer downUser; //다운로드사원
	private Integer updateUser; //등록한 사원
	
}
