package com.workmate.app.vendor.service;

import java.sql.Date;

import lombok.Data;

@Data
public class VendorVO {
	private String vendCode;		// 거래처코드
	private String vendName;		// 거래처이름
	private String vendDetail;		// 거래처설명
	private String bizrNum;			// 사업자번호
	private String rpstrName;		// 대표자이름
	private String biztp;			// 업종
	private String vendAdress;		// 거래처주소
	private String vendPhone;		// 거래처 연락처
	private String vendEmail;		// 거래처 이메일
	private Date cntrStart;			// 계약 시작일
	private Date cntrEnd;			// 계약 만료일
	private String cntrStatus;		// 계약 상태
	private String cntrManager;		// 담당자
	private String condition;		// 특별조건
	private Date regDate;			// 거래처 등록일
	private Integer cntrCount;		// 거래금액
	private String cntrFile;		// 첨부파일
	private String cntrAttachment;   // 서버에 저장된 파일명
	

}
