package com.workmate.app.mail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.workmate.app.employee.service.DepartmentVO;
import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.employee.service.TeamVO;
import com.workmate.app.mail.service.AttachmentVO;
import com.workmate.app.mail.service.MailFolderVO;
import com.workmate.app.mail.service.MailVO;

public interface MailMapper {
	// 받은메일
	List<MailVO> findReceivedMailsList(@Param("userNo") int userNo);

	// 단건
	MailVO findMailById(@Param("mailId") int mailId);

	// 보낸메일 저장
	void insertMail(@Param("mail") MailVO mail);

	// 사원의 이메일을 기반으로 USER_NO를 조회
	@Select("SELECT USER_NO FROM EMPLOYEE WHERE USER_MAIL = #{email}")
	Integer findUserNoByEmail(@Param("email") String email);

	// 보낸 메일 조회
	List<MailVO> findSentMailsList(@Param("userNo") int userNo);

	// 보낸 메일 단건
	MailVO findSentMailById(@Param("mailId") int mailId);

	// 내부 사용자 확인
	MailVO findUserByEmail(@Param("email") String email);

	@Update("UPDATE MAIL SET STATUS = '삭제됨', FOLDER_ID = 1006 WHERE MAIL_ID = #{mailId}")
	void updateMailToTrash(@Param("mailId") int mailId);

	@Select("SELECT * FROM MAIL WHERE USER_NO = #{userNo} AND FOLDER_ID = 1006 ORDER BY SENT_AT DESC")
	List<MailVO> findTrashMails(@Param("userNo") int userNo);

	// 전체 폴더 조회
	List<MailFolderVO> selectMailFolderList(@Param("userNo") int userNo);

	// 폴더 추가
	void insertMailFolder(@Param("folder") MailFolderVO folder);

	// 폴더 삭제
	void deleteMailFolder(@Param("folderId") int folderId, @Param("userNo") int userNo);

	// 특정 사용자 폴더 ID에 해당하는 메일 목록 조회
	List<MailVO> selectMailsByFolderId(@Param("userNo") int userNo, @Param("folderId") int folderId);

	// 메일폴더 이동
	void updateMailFolder(@Param("mailId") int mailId, @Param("folderId") int folderId);

	// 특정 폴더(FOLDER_ID) 안에 있는 메일들만 조회
	List<MailVO> selectMailListByFolderId(@Param("userNo") int userNo, @Param("folderId") int folderId);

	// 특정폴더이름 넘기기(내가만든 폴더)
	MailFolderVO selectMailFolderById(@Param("folderId") int folderId);

	// 메일 완전삭제
	void deleteMail(@Param("mailId") int mailId);

	// 공용 페이징 메일 조회
	List<MailVO> selectMailListPaging(@Param("userNo") int userNo, @Param("folderId") int folderId,
			@Param("offset") int offset, @Param("limit") int limit);

	// 공용 메일 개수 조회
	int countMailsByFolder(@Param("userNo") int userNo, @Param("folderId") int folderId);
	//스팸
	List<MailVO> findSpamMails(@Param("userNo") int userNo);
	//메일 중복체크
	@Select("SELECT COUNT(*) FROM MAIL WHERE MESSAGE_ID = #{messageId}")
	int countMessageId(@Param("messageId") String messageId);

	//전체부서목록 조회
	List<DepartmentVO> selectDepartmentList();
	//특정부서 팀목록
    List<TeamVO> selectTeamListByDepartment(String departmentId);
  //특정 팀에 속한 사원들의 이메일 주소
    List<String> selectEmailsByTeam(String teamNo);
  //특정 팀에 속한 사원들의 정보 전체
    List<EmpVO> selectEmployeesByTeam(String teamNo);
    //예약된 메일 중 발송 시점이 지난 것 조회
    List<MailVO> selectScheduledMails();
    //예약 상태 업데이트
    void updateReserStatus(int mailId, String status);
    //특정 메일(mailId)에 첨부된 파일(Attachment) 목록을 DB에서 조회
    List<AttachmentVO> findAttachmentsByMailId(int mailId);
    //받은 메일 읽음 기능
    @Update("UPDATE MAIL SET IS_READ = 'Y' WHERE MAIL_ID = #{mailId}")
    void updateMailReadStatus(@Param("mailId") int mailId);
    
    @Select("SELECT COUNT(*) FROM MAIL WHERE USER_NO = #{userNo} AND FOLDER_ID = 1001 AND IS_READ = 'N'")
    int countUnreadMails(@Param("userNo") int userNo);
    
    @Select("SELECT USER_PHONE FROM EMPLOYEE WHERE USER_NO = #{userNo}")
    String findPhoneByUserNo(int userNo);
    
}
