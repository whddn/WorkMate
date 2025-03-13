package com.workmate.app.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
					.requestMatchers("/", "/workmate").permitAll()	// 전체 접근 허용
					
					.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // USER, ADMIN이라는 롤을	가진 사용자만 접근 허용
					.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // ROLE_ADMIN이라는 롤을	가진 사용자만 접근 허용
					.anyRequest().authenticated() // 권한 상관없이 인증받은 사용자만 접근 허용
			)
			.formLogin(login -> login
					.loginPage("/login")
				//	.loginProcessingUrl("/workmate")
					.defaultSuccessUrl("/", true)
					.permitAll())
			.logout(logout -> logout
					.logoutSuccessUrl("/workmate")
					.invalidateHttpSession(true));
		http.csrf(csrf -> csrf.disable());
		return http.build();
	}
	
	
 @Bean 
 WebSecurityCustomizer webSecurityCustomizer() {
	 return (web) -> web.ignoring().requestMatchers("/assets/**", "/images/**", "/js/**", "/css/**"); // 예외처리하고 싶은 url
 }
 @Bean
	public UserDetailsService userDetailsService() {
	 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserDetails user =
				User.builder()
				.username("user")
				.password(passwordEncoder.encode("1111"))
				.roles("USER")
				.build();
		UserDetails admin =
				User.builder()
					.username("admin")
					.password(passwordEncoder.encode("1111"))
					.roles("ADMIN")
					.build();

		return new InMemoryUserDetailsManager(user,admin);
	}
 
}
