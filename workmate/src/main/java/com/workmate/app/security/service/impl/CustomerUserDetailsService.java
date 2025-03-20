package com.workmate.app.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.workmate.app.employee.service.EmpVO;
import com.workmate.app.security.mapper.UserMapper;
import com.workmate.app.security.service.UserVO;
import com.workmate.app.security.service.LoginUserVO;
@Service
public class CustomerUserDetailsService implements UserDetailsService{
	private UserMapper userMapper;
	
	@Autowired
	CustomerUserDetailsService(UserMapper userMapper){
		this.userMapper = userMapper;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		EmpVO empVO = userMapper.getUserInfo(username);
		if(empVO == null) {
			throw new UsernameNotFoundException(username);
		}
		return new LoginUserVO(empVO);
	}
	
}
