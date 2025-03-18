package com.workmate.app.approval.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ReportAttachVO {
	private Integer repattachNo;
	private String fileName;
	private String filePath;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date uploadDate;
	private String reportNo;
	private String apprNo;
}
