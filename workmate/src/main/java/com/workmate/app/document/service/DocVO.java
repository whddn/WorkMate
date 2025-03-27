package com.workmate.app.document.service;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DocVO {
	
	// 자료실 
	private int documentNo; 		//자료번호	
	private String fileName; 		//파일명
	private String fileDesc; 		//파일명
	private Long fileSize; 			//파일크기
	private String formattedSize;	//파일크기포맷변환	
	private String attachment; 		//첨부파일
	private String	extenstion; 	//확장자
	private Date fileRegDate; 		//등록날짜
	private String	fileTag; 		//태그
	private Integer userNo; 		//사원번호
	private String TeamNo; 			//팀번호
	private String userId; 			//사원이이디
	private String userName;
	private Integer updateUser; 	//등록한 사원
	
	
	// 다운로드 이력
	private Integer downUser; 	 	//다운로드사원
	private String downloadUserIp; 	 	//다운로드사원
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date downloadDate; 		//다운로드 날짜
	
	private int downloadNo;  		//다운로드 번호	

}
