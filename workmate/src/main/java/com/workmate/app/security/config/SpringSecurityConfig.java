package com.workmate.app.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	@Bean // 비밀번호 암호화
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http //	보호된 리소스 URI에 접근할 수 있는 권한을 설정
			.authorizeHttpRequests((authrize)  
				-> authrize
					//.requestMatchers("/", "/workmate").permitAll()	// 전체 접근 허용
				    .requestMatchers("/header").authenticated()
					//.requestMatchers("/user/**").permitAll() // 모든사용자 접근 허용
					//.requestMatchers("/team/**").hasAuthority("T001")  // 중간 권한

				    // 중간 권한
					.requestMatchers("/attendance/attendanceManage").hasRole("T001")
					.requestMatchers("/emp/bfevalu").hasRole("T001")	// 평가 관리 권한 (인사관리팀)
					// 관리자 권한
					.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // 관리자만 접근 허용 ROLE_ADMIN
					.anyRequest().authenticated() // permitAll 넣을시 모든 페이지 로그인 없이 접근 가능


			)                                 //authenticated 넣을시 권한에 따라 페이지 접근 가능 로그인 필수


			.formLogin(login -> login
				    .loginPage("/login")
				    .defaultSuccessUrl("/", true)
				    .failureUrl("/login?error=true") // 실패 시 error
				    .permitAll())
				.logout(logout -> logout
				    .logoutSuccessUrl("/login?logout") // 로그아웃 시 메시지
				    .invalidateHttpSession(true)
				    .deleteCookies("JSESSIONID")
				    .permitAll());
		
	    //CSRF 토큰 
		http.csrf(csrf -> csrf.disable()); // <-- CSRF수정 바람
		
		http.headers(httpSecurityHeadersConfigurer 
				-> httpSecurityHeadersConfigurer
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));	// X-Frame-Options 동일 경로에서는 호용
		return http.build();
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/assets/**", "/images/**", "/js/**", "/css/**"); // 예외처리하고 싶은
																											// url
	}
	
	/*
	 * @Bean public UserDetailsService userDetailsService() { PasswordEncoder
	 * passwordEncoder = new BCryptPasswordEncoder(); UserDetails user =
	 * User.builder() .username("user") .password(passwordEncoder.encode("1111"))
	 * .roles("USER") .build(); UserDetails admin = User.builder()
	 * .username("admin") .password(passwordEncoder.encode("1111")) .roles("ADMIN")
	 * .build();
	 * 
	 * return new InMemoryUserDetailsManager(user,admin); }
	 */

}
