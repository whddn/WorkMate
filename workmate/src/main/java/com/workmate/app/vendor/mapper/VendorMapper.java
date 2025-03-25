package com.workmate.app.vendor.mapper;

import java.util.List;

import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.vendor.service.VendorVO;

public interface VendorMapper {
	
	// 전체 조회
	public List<VendorVO> selectVendorList(VendorVO vendorVO);
	// 단건 조회
	public VendorVO selectVendorById(VendorVO vendorVO);
	// 등록
	public int insertVendorInfo(VendorVO vendorVO);
	//?
	public int updateVendorStatus(VendorVO vendorVO);
	// 수정
	public int updateVendorInfo(VendorVO vendorVO);
	// 삭제
	public int deleteVendorInfo(VendorVO vendorVO);
}
