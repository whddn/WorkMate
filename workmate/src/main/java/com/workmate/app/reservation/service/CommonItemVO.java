package com.workmate.app.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CommonItemVO {
	private Integer commonNo;			// 공용품번호
	private String name;				// 이름
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime reserStartTime;	// 예약시작시간
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime reserEndTime;		// 예약종료시간
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateSelect;		// 날짜선택
	private String commonKind;			// 공용품종류
	private String image;				// 이미지(DB에 저장할 파일 경로)
	private String content;				// 설명
	private String usageStatus;			// 사용여부
	
	private String reserStartTimeStr;	// 예약시작시간string변환
	private String reserEndTimeStr;		// 예약종료시간string변환
	private MultipartFile itemImage;	// 업로드할 이미지파일(DB저장x)
}
