package com.example.demo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@SpringBootTest
public class PasswordEncoderTest {
   

	@Test
    public void run1()  {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "1111";  // 원하는 간단한 비밀번호 설정
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("암호화된 비밀번호: " + encodedPassword);
    }
}
