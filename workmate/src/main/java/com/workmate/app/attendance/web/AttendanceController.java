package com.workmate.app.attendance.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workmate.app.attendance.service.AttendanceService;
import com.workmate.app.attendance.service.WorkVO;
import com.workmate.app.common.FileUtil;
import com.workmate.app.security.service.LoginUserVO;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
/**
 * 근태관리 / 연차관리
 * @author 박지희
 * @since 2025-03-10
 * <pre>
 * <pre>
 * 수정일자           수정자          수정일자
 * ------------------------------------------
 * 
 * 
 * </pre> 
 *  
 */
@RequestMapping("attendance")
@Controller
@RequiredArgsConstructor
public class AttendanceController {
		
	private final AttendanceService attendService; 
	
//	@Autowired
//	public AttendanceController(AttendanceService attendService) {
//		this.attendService = attendService;
//	}
	
	/**
	 * 이번달 근태 조회 페이지 이동
	 * @param WorkVO
	 * @param loginUser
	 * @return 출퇴근 이번달근태조회
	 */
	//월별 근태 전체조회 : GET	
	@GetMapping("/monthList")
	public String monthAttendanceList(Model model, @AuthenticationPrincipal LoginUserVO loginUser) {		
	    
//		if (loginUser == null || loginUser.getUserVO() == null) {
//	        return "redirect:/login";  // 로그인 페이지로 리다이렉트
//	    }
	    
		//로그인 여부 확인
		int userNo = loginUser.getUserVO().getUserNo(); 		
		String userName = loginUser.getUserVO().getUserName();
		
		//2)Service
		//현재월 근태 데이터 조회
		List<WorkVO> list = attendService.findMonthWork(userNo);
		int monthTotal = 209;
		double addWorkTime = list.stream().mapToDouble(WorkVO::getAddWorkTime).sum();
		double totalWorkTime = list.stream().mapToDouble(WorkVO::getWorkTime).sum();
		double remainWorkTime = monthTotal - totalWorkTime;
		
		//2-1)Service > View 전달
		model.addAttribute("works", list);
		//출근여부 userNO에 담기
		model.addAttribute("attend",attendService.attendanceStatus(userNo));
		model.addAttribute("totalWorkTime", totalWorkTime);
		model.addAttribute("addWorkTime", addWorkTime); 
		model.addAttribute("remainWorkTime", remainWorkTime);
		//
		model.addAttribute("userNo", userNo);
		model.addAttribute("userName", userName);
		
		return "attendance/monthList";
	}
	/**
	 * 내 전체 근태 조회 페이지로 이동
	 * @param model
	 * @param workVO
	 * @param loginUser
	 * @return 내전체근태조회
	 */
	//전체 근태 조회
	@GetMapping("/allList")
	public String allAttendanceList(Model model, 
			                        WorkVO workVO, 
			                        @AuthenticationPrincipal LoginUserVO loginUser) {
		
		//로그인 여부
//		if (loginUser == null || loginUser.getUserVO() == null) {
//		 return "redirect:/login";
//		}
		
		int userNo = loginUser.getUserVO().getUserNo();
		workVO.setUserNo(userNo);
		
		List<WorkVO> list = attendService.findAllWork(workVO);
		model.addAttribute("works", list);

		return "attendance/allList";
	}
	

	//출근 등록
	@GetMapping("/startWork")
	public String startWork(WorkVO workVO , 
					        @AuthenticationPrincipal LoginUserVO loginUser ,
					        RedirectAttributes rttr
					        ) {
		
		int userNo = loginUser.getUserVO().getUserNo();
		workVO.setUserNo(userNo);		
		attendService.startWork(workVO);
		rttr.addFlashAttribute("msg", "출근등록되었습니다.");
		return "redirect:/attendance/monthList";		
	}
	 
	//퇴근 등록
	@GetMapping("/afterWork")
	public String afterWork(WorkVO workVO,
							@AuthenticationPrincipal LoginUserVO loginUser ,
							RedirectAttributes rttr) {
		int userNo = loginUser.getUserVO().getUserNo();
		workVO.setUserNo(userNo);
		int after = attendService.afterWork(workVO);
		rttr.addFlashAttribute("msg", "퇴근등록되었습니다.");
		return "redirect:/attendance/monthList";
	}
	
	//지각사유업로드
	@PostMapping("/lateReason")
	public String lateReason(WorkVO workVO,
							@AuthenticationPrincipal LoginUserVO loginUser ,
							RedirectAttributes rttr) {
		
		int userNo = loginUser.getUserVO().getUserNo();
		workVO.setUserNo(userNo);
		
		 // 지각 사유 저장하는 서비스 호출
	    attendService.inputLateReason(workVO); 
		
		rttr.addFlashAttribute("msg", "지각사유가 제출되었습니다.");
		return "redirect:/attendance/monthList";
	}
	
	//내 발생 연차, 연차사용내역전체조회
	@GetMapping("/annual")
	public String annualList(Model model, WorkVO workVO, @AuthenticationPrincipal LoginUserVO loginUser) {
		
		//로그인 여부				
		int userNo = loginUser.getUserVO().getUserNo();
		workVO.setUserNo(userNo);	
		
		List<WorkVO> list = attendService.findOccAnnual(workVO);
		List<WorkVO> alist = attendService.findAllAnnual(workVO);		
		
		model.addAttribute("occs", list);
		model.addAttribute("anls", alist);
		
		return "attendance/annual";
	}
	
	
	
	//전체사원 근태조회()
	@GetMapping("/attendanceManage")
	public String attendEmpList(Model model, WorkVO workVO) {
		
		List<WorkVO> count = attendService.findMothEmpWork(workVO);
		List<WorkVO> list = attendService.findAllEmpWork(workVO);
		
		model.addAttribute("works", list);
		model.addAttribute("counts", count);
		
		return "attendance/attendanceManage";
	}
	
	//엑셀 다운로드
	@GetMapping("/export/excel")
	public void exportToExcel(HttpServletResponse response, WorkVO workVO)throws IOException {
		// 파일명
		String fileName = "근태현황.xlsx";
		
		// 헤더
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
	    
	    // 엑셀 워크북 생성
	    XSSFWorkbook workbook = new XSSFWorkbook();
	    XSSFSheet sheet = workbook.createSheet("목록");
	    
	    // 날짜 포맷 스타일 지정
	    CellStyle dateCellStyle = workbook.createCellStyle();
	    CreationHelper createHelper = workbook.getCreationHelper();
	    dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
	    
	    Row header = sheet.createRow(0);
	    header.createCell(0).setCellValue("팀명");
	    header.createCell(1).setCellValue("이름");
	    header.createCell(2).setCellValue("출근시간");
	    header.createCell(3).setCellValue("퇴근시간");
	    header.createCell(4).setCellValue("근무시간");
	    header.createCell(5).setCellValue("연장근무시간");
	    header.createCell(6).setCellValue("근무상태");
	    
	    List<WorkVO> lists = attendService.findAllEmpWork(workVO);
	    
	    int rowNum = 1;
	    for (WorkVO list : lists) {
	    	Row row = sheet.createRow(rowNum++);
	    	row.createCell(0).setCellValue(list.getTeamName());
	        row.createCell(1).setCellValue(list.getUserName());
	        
	        Cell startCell = row.createCell(2);
	        startCell.setCellValue(list.getStartWork());
	        startCell.setCellStyle(dateCellStyle);
	        
	        Cell afterCell = row.createCell(3);	        
	        afterCell.setCellValue(list.getAfterWork());
	        afterCell.setCellStyle(dateCellStyle);
	        
	        row.createCell(4).setCellValue(list.getWorkTime());
	        row.createCell(5).setCellValue(list.getAddWorkTime());
	        row.createCell(6).setCellValue(list.getWorkStatus());
	    }
	    
	    workbook.write(response.getOutputStream());
	    workbook.close();
	}
	
	//필터링 된 엑셀다운
	@GetMapping("/export/excel/filter")
	public void downExel(@RequestParam(required = false) String stDate,
						@RequestParam(required = false) String endDate,
						@RequestParam(required = false) String userName,
						@RequestParam(required = false) String teamName,
						HttpServletResponse response) throws IOException {
		List<WorkVO> list = attendService.getFilterAttend(stDate, endDate, userName, teamName);
		
		 // Excel 파일명 설정
	    String fileName = FileUtil.today() + "_근태_목록.xlsx";
	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

	    XSSFWorkbook workbook = new XSSFWorkbook();
	    XSSFSheet sheet = workbook.createSheet("근태 목록");

	    // 날짜 스타일 지정
	    CellStyle dateCellStyle = workbook.createCellStyle();
	    CreationHelper createHelper = workbook.getCreationHelper();
	    dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));

	    // 헤더 작성
	    Row header = sheet.createRow(0);
	    header.createCell(0).setCellValue("팀명");
	    header.createCell(1).setCellValue("이름");
	    header.createCell(2).setCellValue("출근시간");
	    header.createCell(3).setCellValue("퇴근시간");
	    header.createCell(4).setCellValue("근무시간");
	    header.createCell(5).setCellValue("연장근무");
	    header.createCell(6).setCellValue("근태상태");

	    // 데이터 작성
	    int rowNum = 1;
	    for (WorkVO vo : list) {
	        Row row = sheet.createRow(rowNum++);
	        row.createCell(0).setCellValue(vo.getTeamName());
	        row.createCell(1).setCellValue(vo.getUserName());

	        Cell cell2 = row.createCell(2);
	        cell2.setCellValue(vo.getStartWork()); // Date 타입일 경우
	        cell2.setCellStyle(dateCellStyle);

	        Cell cell3 = row.createCell(3);
	        cell3.setCellValue(vo.getAfterWork());
	        cell3.setCellStyle(dateCellStyle);

	        row.createCell(4).setCellValue(vo.getWorkTime());
	        row.createCell(5).setCellValue(vo.getAddWorkTime());
	        row.createCell(6).setCellValue(vo.getWorkStatus());
	    }

	    // 엑셀 출력
	    workbook.write(response.getOutputStream());
	    workbook.close();
	}
	
	
}
