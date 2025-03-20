package com.workmate.app.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.admin.service.AdminService;
import com.workmate.app.reservation.mapper.CommonItemMapper;
import com.workmate.app.reservation.service.CommonItemVO;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	//필드주입 방법
	private CommonItemMapper commonitemMapper;
	
	/*
	 * 생성자 방식 public AdminServiceImpl(CommonItemMapper commonitemMapper) {
	 * this.commonitemMapper = commonitemMapper; }
	 */

	// 공용품 전체조회
	@Override
	public List<CommonItemVO> selectItemList() {
		return commonitemMapper.selectItemList();
	}

	// 공용품 단건조회
	@Override
	public CommonItemVO findItemInfo(CommonItemVO commonitemVO) {
		return commonitemMapper.selectItemById(commonitemVO);
	}

	// 공용품생성
	@Override
	public int createCommonItemInfo(CommonItemVO commonitemVO) {
		int result = commonitemMapper.insertCommonItemInfo(commonitemVO);
		return result == 1 ? commonitemVO.getCommonNo() : -1;
	}

	// 공용품수정
	@Override
	public Map<String, Object> modifyItemInfo(CommonItemVO commonitemVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		int result = commonitemMapper.updateItemInfo(commonitemVO);
		if(result == 1) {
			isSuccessed = true;
		}
		map.put("result", isSuccessed);
		map.put("target", commonitemVO);
		
		return map;
	}

	// 공용품삭제
	@Override
	public Map<String, Object> removeItemInfo(int commonNo) {
		Map<String, Object> map = new HashMap<>();
		int result = commonitemMapper.deleteItemInfo(commonNo);
		if(result == 1) {
			map.put("commonNo", commonNo);
		}
		return map;
	}

}
