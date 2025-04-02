package com.workmate.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.workmate.app.mail.service.MailService;

@SpringBootTest
public class DemoApplicationTest {
	@Autowired MailService mailService;
	
	@Test
	public void testInsert() {
		mailService.fetchAndStoreEmails();
		
	}
}
