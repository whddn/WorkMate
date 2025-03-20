package com.workmate.app.mail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.workmate.app.mail.service.MailVO;

public interface MailMapper {
	//받은메일
	 List<MailVO> getReceivedMails(@Param("userNo") int userNo);


	//단건
	MailVO getMailById(@Param("mailId") int mailId);
	//보낸메일 저장
	void insertMail(@Param("mail") MailVO mail);
	//사원의 이메일을 기반으로 USER_NO를 조회
	@Select("SELECT USER_NO FROM EMPLOYEE WHERE USER_MAIL = #{email}")
	int findUserNoByEmail(@Param("email") String email);
	// 보낸 메일 조회
    List<MailVO> getSentMails(@Param("userNo") int userNo);
 // 보낸 메일 단건
    MailVO getSentMailById(@Param("mailId") int mailId);
    // 내부 사용자 확인
    Integer findUserByEmail(@Param("email") String email);
}
