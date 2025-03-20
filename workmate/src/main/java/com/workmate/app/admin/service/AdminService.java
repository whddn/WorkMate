package com.workmate.app.admin.service;

import java.util.List;
import java.util.Map;
import com.workmate.app.reservation.service.CommonItemVO;

public interface AdminService {
	// 전체조회
	public List<CommonItemVO> findItemList();
	//단건조회
	public CommonItemVO findItemById(CommonItemVO commonitemVO);
	// 공용품 등록
	public int inputCommonItem(CommonItemVO commonitemVO);
	// 수정
	public Map<String, Object>modifyItem(CommonItemVO commonitemVO);
	// 삭제
	public Map<String, Object>dropItem(int commonNo);
}
