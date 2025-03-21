package com.workmate.app.mainscreen.service;

import lombok.Data;

@Data
public class MenuVO {
	private Integer menuNo;		// 메뉴번호
	private String menuTitle;	// 메뉴명
	private String menuLink;	// 링크
	
	private String searchKeyword;	// 메뉴 검색어
}
