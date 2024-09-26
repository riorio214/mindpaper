package com.paper.demo.user.service;

import com.paper.demo.auth.service.dto.AuthDto;
import com.paper.demo.common.admin.AdminException;
import com.paper.demo.user.domain.User;

public interface IUserServiceV1 {
	String getUserName(String email);
	String getOauthUserName(String email);
	void registerUser(AuthDto.SignupDto signupDto );
	void checkPasswordCompatibility(String password) throws AdminException;
	void checkDuplicatedEmail(String email);
	void checkPassword(String password);
	void checkEmailCompatibility(String email);
	void registerAdmin(AuthDto.SignupDto signupDto);
	void checkEmailInDB(String email);

	String getUserEmail();
}
