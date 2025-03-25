package com.workmate.app.vendor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.approval.mapper.ApprovalMapper;
import com.workmate.app.approval.service.ApprovalVO;
import com.workmate.app.vendor.mapper.VendorMapper;
import com.workmate.app.vendor.service.VendorService;
import com.workmate.app.vendor.service.VendorVO;

@Service
public class VendorServiceImpl implements VendorService {

	private VendorMapper vendorMapper;
	private ApprovalMapper approvalMapper;
	
	@Autowired
	VendorServiceImpl(VendorMapper vendorMapper, ApprovalMapper approvalMapper){
		this.vendorMapper = vendorMapper;
		this.approvalMapper = approvalMapper;
	}
	
	// 전체
	@Override
	public List<VendorVO> findVendorList(VendorVO vendorVO) {
		return vendorMapper.selectVendorList(vendorVO);
	}

	// 단건
	@Override
	public VendorVO findVendorById(VendorVO vendorVO) {
		return vendorMapper.selectVendorById(vendorVO);
	}

	// 등록
	@Override
	public int inputVendor(VendorVO vendorVO) {
		return vendorMapper.insertVendorInfo(vendorVO);
	}

	// 수정
//	@Override
//	public int modifyVendor(VendorVO vendorVO) {
//		int result = vendorMapper.updateVendorStatus(vendorVO);
//		if(result <1) {
//			return 0;
//		}
//		ApprovalVO approval = approvalMapper.selectApprovalById(approvalVO);
//		if("a2".equals(approval.getApprStatus()) && "AF002".equals(approval.getApprType())) {
//			vendorMapper.updateVendorStatus(approvalVO)
//		}
//		return approvalMapper.updateApprovalDate(approvalVO);
//	}
	
	@Override
	public int modifyVendor(VendorVO vendorVO) {
		// TODO Auto-generated method stub
		return 0;
	}


	// 삭제
	@Override
	public int dropVendor(VendorVO vendorVO) {
		return vendorMapper.deleteVendorInfo(vendorVO);
	}

	

}
