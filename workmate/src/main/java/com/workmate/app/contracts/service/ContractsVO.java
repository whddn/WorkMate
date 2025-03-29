package com.workmate.app.contracts.service;

import java.util.Date;

import lombok.Data;

@Data
public class ContractsVO {
	private String contrNo;
	private String contrTitle;
	private String contrContent;
	private String contrType;
	private String contrStatus;
	private String contrA;
	private String contrB;
	private Date createDate;
	private Date signDateA;
	private Date signDateB;
	private Date contrStart;
	private Date contrEnd;
	private String vendCode;
	
	private byte[] signImage; // 서명 이미지 (BLOB)
	private String signImageBase64; // 프론트에서 넘어온 base64 문자열

}
