package com.workmate.app.mail.mapper;

import java.util.List;

public interface EmployeeMapper {
	List<String> selectEmailsByTeam(String teamNo);
}
