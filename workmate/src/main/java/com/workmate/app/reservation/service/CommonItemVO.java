package com.workmate.app.reservation.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CommonItemVO {
	private Integer commonNo;			// 공용품번호
	private String name;				// 이름
	
	@DateTimeFormat(pattern = "HH:mm")
	private Date reserStartTime;		// 예약시작시간
	
	@DateTimeFormat(pattern = "HH:mm")
	private Date reserEndTime;			// 예약종료시간
	
	private Date dateSelect;			// 날짜선택
	private String commonKind;			// 공용품종류
	private String image;				// 이미지
	private String content;				// 설명
	private String usageStatus;			// 사용여부
}
