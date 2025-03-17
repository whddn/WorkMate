package com.workmate.app.approval.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprLineVO {
	private Integer apprlineNo;
	private String insertTitle;	// 즐겨찾는 결재선 제목
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date insertDate;	// 즐겨찾는 결재선 등록일 
	private Integer inserter;	// 즐겨찾는 결재선 등록자
	private String components;	// 결재선을 이루는 사원번호들
	
	private String userName;	// 등록자의 이름
	
	public void setComponentByList(List<Integer> list) {
		components = "";
		for(int i = 0; i < list.size(); i++) {
			components += list.get(i);
			if(i < list.size() - 1) {
				components += ",";
			}
		}
	}
	public List<Integer> getComponentsByList() {
		List<String> list1 = Arrays.asList(components.split(","));
		List<Integer> list2 = new ArrayList<>();
		for(String item : list1) {
			list2.add(Integer.parseInt(item));
		}
		return list2;
	}
}
