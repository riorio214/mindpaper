package com.paper.demo.user.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.paper.demo.auth.service.dto.AuthDto;
import com.paper.demo.user.details.UserDetailsImpl;
import com.paper.demo.user.domain.Role;
import com.paper.demo.user.domain.User;
import com.paper.demo.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements IUserServiceV1 {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/*
	* 관리자 등록
	 */
	@Override
	public void registerAdmin(AuthDto.SignupDto signupDto) {
		//이메일 중복 체크
		checkDuplicatedEmail(signupDto.getEmail());
		checkEmailCompatibility(signupDto.getEmail());

		//비밀번호 입력안내와 호환성검사
		checkPassword(signupDto.getPassword());
		checkPasswordCompatibility(signupDto.getPassword());

		String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
		User user = User.registerAdmin(signupDto.getEmail(), encodedPassword,signupDto.getName() ,Role.ADMIN);
		userRepository.save(user);
	}
	/*
	* 사용자 등록
	 */
	@Override
	public void registerUser(AuthDto.SignupDto signupDto){
		checkDuplicatedEmail(signupDto.getEmail());
		checkEmailCompatibility(signupDto.getEmail());
		checkPassword(signupDto.getPassword());
		checkPasswordCompatibility(signupDto.getPassword());
		String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
		User user = User.registerUser(signupDto.getEmail(), encodedPassword, signupDto.getName(), Role.USER);
		userRepository.save(user);
	}

	//이메일 호환성검사 ( 이메일 형식 )
	@Override
	public void checkEmailCompatibility(String email) {
		if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
			System.out.println("email = " + email);
			throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
		}
		System.out.println("email = " + email);
	}
	//비밀번호 호환성검사 ( 숫자 + 문자 + 특수문자 10자 이상 )
	@Override
	public void checkPasswordCompatibility(String password) {
		if (!password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{10,}$")) {
			throw new IllegalArgumentException("비밀번호는 숫자, 문자, 특수문자를 포함하여 10자 이상이어야 합니다.");
		}
	}
	//비밀번호 입력 안내
	@Override
	public void checkPassword(String password) {
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("비밀번호를 입력해주세요.");
		}
	}
	/*
	* 이메일로 사용자 조회
	* 만약 이메일로 사용자가 있다면 예외를 발생시킨다.
	 */
	@Override
	public void checkDuplicatedEmail(String email) {
		userRepository.findByEmail(email)
			.ifPresent(user -> {
				throw new IllegalArgumentException("일치하는 정보가 없습니다.");
			});
	}

	@Override
	public String getUserName(String email){
		return userRepository.findUserNameByEmail(email);
	}
	@Override
	public String getOauthUserName(String email){
		return userRepository.findOAuthUserNameByEmail(email);
	}

	@Override
	public String getUserEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new RuntimeException("인증이 불가 합니다.");
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetailsImpl userDetails) {
			return userDetails.getUsername(); // 이메일 주소 반환
		} else if (principal instanceof String) {
			return (String) principal;
		} else {
			throw new RuntimeException("토큰 값이 일치하지 않습니다. " + principal.getClass().getName());
		}
	}

	@Override
	public void checkEmailInDB(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new IllegalArgumentException("일치하는 정보가 없습니다.");
		}
	}
}
