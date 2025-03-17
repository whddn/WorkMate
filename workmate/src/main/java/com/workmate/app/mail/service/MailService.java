package com.workmate.app.mail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workmate.app.mail.mapper.MailMapper;

@Service
public class MailService {
	private final MailMapper mailMapper;

    @Autowired
    public MailService(MailMapper mailMapper) {
        this.mailMapper = mailMapper;
    }

    public List<MailVO> getReceivedMails(int userNo) {
        return mailMapper.getReceivedMails(userNo);
    }
    
}
