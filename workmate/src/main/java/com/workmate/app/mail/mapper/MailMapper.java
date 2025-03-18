package com.workmate.app.mail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workmate.app.mail.service.MailVO;

public interface MailMapper {
	//받은메일
	 List<MailVO> getReceivedMails(@Param("userNo") int userNo);


	//단건
	MailVO getMailById(@Param("mailId") int mailId);
}
