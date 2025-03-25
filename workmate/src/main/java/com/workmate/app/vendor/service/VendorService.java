package com.workmate.app.vendor.service;

import java.util.List;

public interface VendorService {
	// 전체 조회
	public List<VendorVO> findVendorList(VendorVO vendorVO);
	// 단건 조회
	public VendorVO findVendorById(VendorVO vendorVO);
	// 등록
	public int inputVendor(VendorVO vendorVO);
	// 수정
	public int modifyVendor(VendorVO vendorVO);
	// 삭제
	public int dropVendor(VendorVO vendorVO);
}
