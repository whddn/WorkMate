package com.workmate.app.contracts.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ContractsVO {
	private String contrNo;			// 계약번호
	private String contrTitle;		// 계약제목
	private String contrContent;	// 계약내용
	private String contrType;		// 계약유형
	private String contrStatus;		// 계약상태
	private String contrA;			// 계약당사자 갑
	private String contrB;			// 계약당사자 을
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;		// 작성일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date signDateA;			// 갑 서명일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date signDateB;			// 을 서명일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date contrStart;		// 계약시작일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date contrEnd;			// 계약종료일
	private String vendCode;		// 거래처코드
	private String cntrLocation;	// 근무장소
	private String cntrMoney;		//  월급
	private String cntrFile;		// 첨부파일
	private String cntrAttachment;   // 서버에 저장된 파일명
	
	
	private byte[] signImage; // 서명 이미지 (BLOB)
	private String signImageBase64; // 프론트에서 넘어온 base64 문자열

}
