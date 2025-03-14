package com.workmate.app.approval.service;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ApprLineVO {
	private Integer apprlineNo;
	private String insertTitle;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date insertDate;
	private Integer inserter;
	private String apprNo;
}
