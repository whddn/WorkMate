package com.workmate.app.mainscreen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.mainscreen.mapper.MenuMapper;
import com.workmate.app.mainscreen.service.MenuService;
import com.workmate.app.mainscreen.service.MenuVO;

@Service
public class MenuServiceImpl implements MenuService {
	private MenuMapper menuMapper;
	
	@Autowired
	MenuServiceImpl(MenuMapper menuMapper) {
		this.menuMapper = menuMapper;
	}
	
	@Override
	public List<MenuVO> findMenuList(MenuVO menuVO) {
		// TODO Auto-generated method stub
		return menuMapper.selectMenuList(menuVO);
	}

}
