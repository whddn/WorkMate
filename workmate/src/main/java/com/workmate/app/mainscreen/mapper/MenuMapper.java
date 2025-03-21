package com.workmate.app.mainscreen.mapper;

import java.util.List;

import com.workmate.app.mainscreen.service.MenuVO;

public interface MenuMapper {
	public List<MenuVO> selectMenuList(MenuVO menuVO);
}
