package com.workmate.app.reservation.mapper;

import java.util.List;

import com.workmate.app.reservation.service.CommonItemVO;

public interface CommonItemMapper {
	// 공용품 전체조회
	public List<CommonItemVO> selectItemList();
	// 공용품 단건조회
	public CommonItemVO selectItemById(CommonItemVO commonitemVO);
	// 공용품등록
	public int insertCommonItemInfo(CommonItemVO commonitemVO);
	// 공용품수정
	public int updateItemInfo(CommonItemVO commonitemVO);
	// 공용품삭제
	public int deleteItemInfo(int commonNo);

}
