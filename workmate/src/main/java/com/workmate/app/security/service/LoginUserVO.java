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
		auths.add(new SimpleGrantedAuthority(userVO.getTeamNo()));
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