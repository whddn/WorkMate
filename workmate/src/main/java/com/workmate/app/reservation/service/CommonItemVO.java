package com.workmate.app.reservation.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CommonItemVO {
	private Integer commonNo;
	private String name;
	private String item;
	
	@DateTimeFormat(pattern = "HH:mm")
	private Date reservationStartTime;
	
	@DateTimeFormat(pattern = "HH:mm")
	private Date reservationEndTime;
	
	private Date dateSelect;
	private String commonKind;
	private String image;
	private String content;
}
