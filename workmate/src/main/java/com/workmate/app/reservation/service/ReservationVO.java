package com.workmate.app.reservation.service;

import java.util.Date;

import lombok.Data;

@Data
public class ReservationVO {
	private Integer reser_no;			// 예약번호
	private String name; 				// 이름
	private Integer user_no;			// 사원번호
	private String reser_type;			// 예약종류
	private Date reser_date;			// 예약날짜
	private Date reser_start_time;		// 예약시작시간
	private Date reser_end_time;		// 예약종료시간
	private String reser_availability;	// 예약가능여부
	private String content;				// 내용
	private String common_no;			// 공용품번호
}
