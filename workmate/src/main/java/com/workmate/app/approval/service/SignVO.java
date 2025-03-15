package com.workmate.app.approval.service;

import lombok.Data;

@Data
public class SignVO {
	private Integer signNo;
	private String signTitle;
	private String signPath;
	private String insertDate;
	private Integer userNo;
}
