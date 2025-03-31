package com.workmate.app.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.workmate.app.employee.service.EmpVO;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

@RequiredArgsConstructor
@Getter

public class LoginUserVO implements UserDetails{	
	private final EmpVO userVO;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();
		if(userVO.getTeamNo() != null) {
			auths.add(new SimpleGrantedAuthority(userVO.getTeamNo() ));
		}else {
			auths.add(new SimpleGrantedAuthority("T000"));
		}
		//auths.add(new SimpleGrantedAuthority(userVO.getUserPosition()));
		
		if ("관리자".equals(userVO.getUserPosition())) {
	        auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	    }
		
		return auths;
	}

	@Override
	public String getPassword() {
		return userVO.getUserPwd();
	}

	@Override
	public String getUsername() {
		return userVO.getUserId();
	}

}