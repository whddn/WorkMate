package com.workmate.app.attendance.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class AttendanceVO {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date hireDate;
}
