package com.workmate.app;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
// Advice 클래스 = 공통코드
// Aspect 클래스 = Advice + pointcut
@Aspect
@Log4j2
public class BeforeAdvice {
	//private Logger logger = Logger.getLogger(BeforeAdvice.class);
	//protected static final Logger logger = LogManager.getLogger(BeforeAdvice.class);
	
	@Pointcut("execution(* com.workmate..*Impl.*(..))")
	public void  allpointcut() {}
	
	@Before("allpointcut()")
	public void beforeLog(JoinPoint jp) {
		//메서드명 
		String methodName = jp.getSignature().getName();
		methodName += ":" + jp.toLongString();
		methodName += ":" + jp.toShortString();
		
		//인수(argument)
		Object[] args = jp.getArgs();
		Object arg1 =  (args != null && args.length>0 ? args[0] : "") ; 
		log.debug("[사전처리로그] beforeLog" + methodName +" \n arg:" + arg1);
		//System.out.println("[사전처리] beforeLog" + methodName +" \n arg:" + arg1);
	}
}
