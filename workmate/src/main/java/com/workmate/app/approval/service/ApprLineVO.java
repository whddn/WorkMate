package com.workmate.app.approval.service;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ApprLineVO {
	private Integer apprlineNo;
	private String insertTitle;	// 즐겨찾는 결재선 제목
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date insertDate;	// 즐겨찾는 결재선 등록일 
	private Integer inserter;	// 즐겨찾는 결재선 등록자
	private String apprNo;		// 안씀
	
	private String userName;	// 등록자의 이름
}
