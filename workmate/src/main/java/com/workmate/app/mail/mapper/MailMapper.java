package com.workmate.app.mail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workmate.app.mail.service.MailVO;

public interface MailMapper {
	 List<MailVO> getReceivedMails(@Param("userNo") String userNo);

	List<MailVO> getReceivedMails(int userNo);
}
