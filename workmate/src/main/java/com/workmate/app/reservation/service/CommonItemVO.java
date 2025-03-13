package com.workmate.app.reservation.service;

import java.util.Date;

import lombok.Data;

@Data
public class CommonItemVO {
	private Integer commonNo;
	private String name;
	private Date reservationStartTime;
	private Date reservationEndTime;
	private Date dateSelect;
	private String commonKind;
}
