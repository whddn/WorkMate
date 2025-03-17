package com.workmate.app.reservation.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ReservationVO {
	private Integer reserNo;			// 예약번호
	private String name; 				// 이름
	private Integer userNo;				// 사원번호
	private String commonKind;			// 공용품종류
	private Date reserDate;				// 예약날짜
	@DateTimeFormat(pattern = "HH:mm")	
	private Date reserStartTime;		// 예약시작시간
	@DateTimeFormat(pattern = "HH:mm")
	private Date reserEndTime;			// 예약종료시간
	private String reserAvailability;	// 예약가능여부
	private String content;				// 내용
	private String commonNo;			// 공용품번호
	private String image;				// 이미지
	private String usageStatus;			// 사용여부
	
	
}
