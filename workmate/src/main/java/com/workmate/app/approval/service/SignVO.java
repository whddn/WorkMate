package com.workmate.app.approval.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignVO {
	private Integer signNo;
	private String signTitle;
	private String signPath;
	private String insertDate;
	private Integer userNo;
}
