package com.workmate.app.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
 public static String today() {
	// 현재 날짜/시간
     Date now = new Date();
    
     // 현재 날짜/시간 출력
     System.out.println(now); // Thu May 03 14:43:32 KST 2022
     
     // 포맷팅 정의
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     
     // 포맷팅 적용
     String formatedNow = formatter.format(now);
     
     // 포맷팅 현재 날짜/시간 출력
     return formatedNow;
 }
 
 
 //포맷설정가능
 public static String today(String format) {
	// 현재 날짜/시간
     Date now = new Date();
    
     // 현재 날짜/시간 출력
     System.out.println(now); // Thu May 03 14:43:32 KST 2022
     
     // 포맷팅 정의
     SimpleDateFormat formatter = new SimpleDateFormat(format);
     
     // 포맷팅 적용
     String formatedNow = formatter.format(now);
     
     // 포맷팅 현재 날짜/시간 출력
     return formatedNow;
 }
}
