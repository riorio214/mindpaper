package com.paper.demo.auth.service.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.paper.demo.auth.jwt.JwtTokenProvider;
import com.paper.demo.auth.service.dto.AuthDto;
import com.paper.demo.auth.service.service.AuthService;
import com.paper.demo.common.ResponseStatus;
import com.paper.demo.common.SuccessResponse;
import com.paper.demo.user.details.UserDetailsImpl;
import com.paper.demo.user.domain.User;
import com.paper.demo.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthApiController implements IAuthApiControllerV1 {

	private final AuthService authService;
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;


	private final long COOKIE_EXPIRATION = 7776000; // 90일


	// 회원가입
	public ResponseEntity<?> signupUser(@RequestBody @Valid AuthDto.SignupDto signupDto) {
		userService.registerUser(signupDto);
		SuccessResponse<?> successResponse = SuccessResponse.from(ResponseStatus.SUCCESS,"환영합니다");
		return ResponseEntity
			.status(ResponseStatus.SUCCESS.getCode())
			.body(successResponse);
	}

	public ResponseEntity<?> signupAdmin(@RequestBody @Valid AuthDto.SignupDto signupDto) {
		userService.registerAdmin(signupDto);
		SuccessResponse<?> successResponse = SuccessResponse.from(ResponseStatus.SUCCESS, "환영합니다");
		return ResponseEntity
			.status(ResponseStatus.SUCCESS.getCode())
			.body(successResponse);
	}

	// 로그인 -> 토큰 발급
	public ResponseEntity<?> login(@RequestBody @Valid AuthDto.LoginDto loginDto) throws
		NoSuchAlgorithmException,
		InvalidKeySpecException {
		AuthDto.TokenDto tokenDto = authService.login(loginDto);
		// RT 저장
		HttpCookie httpCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
			.maxAge(COOKIE_EXPIRATION)
			.httpOnly(true)
			.secure(false)
			.build();

		Map<String, String> tokens = new HashMap<>();
		tokens.put("accessToken", tokenDto.getAccessToken());
		tokens.put("refreshToken", tokenDto.getRefreshToken());
		// SuccessResponse 객체 생성
		SuccessResponse<?> successResponse = SuccessResponse.from(ResponseStatus.SUCCESS, tokens);

		// ResponseEntity에 SuccessResponse 및 쿠키, 헤더 추가
		return ResponseEntity
			.status(ResponseStatus.SUCCESS.getCode())
			.header(HttpHeaders.SET_COOKIE, httpCookie.toString()) // 쿠키 설정
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken()) // 액세스 토큰을 헤더에 추가
			.body(successResponse); // SuccessResponse 객체를 바디에 추가
	}
	//true = 재발급O
	//false = 재발급x
	public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
		Map<String, String> tokenMessage = new HashMap<>();
		if (!authService.validate(requestAccessToken)) {
			tokenMessage.put("message", "토큰이 유효합니다.");

			SuccessResponse<?> successResponse = SuccessResponse.from(ResponseStatus.SUCCESS, tokenMessage);
			return ResponseEntity
				.status(ResponseStatus.SUCCESS.getCode())
				.body(successResponse);
			// 토큰이 유효하지 않을 경우
		}else{
			tokenMessage.put("message", "토큰이 유효하지 않습니다.");
			SuccessResponse<?> failResponse = SuccessResponse.from(ResponseStatus.TOKEN_UNAUTHORIZED, tokenMessage);
			System.out.println("토큰이 유효하지 않습니다.");
			return ResponseEntity
				.status(ResponseStatus.TOKEN_UNAUTHORIZED.getCode())
				.body(failResponse);
		}
	}

	// 토큰 재발급
	public ResponseEntity<?> reissue(@CookieValue(name = "refreshToken") String requestRefreshToken,
		@RequestHeader("Authorization") String requestAccessToken) throws
		NoSuchAlgorithmException,
		InvalidKeySpecException {
		Map<String, String> tokens = new HashMap<>();
		AuthDto.TokenDto reissuedTokenDto = authService.reissue(requestAccessToken, requestRefreshToken);
		if (reissuedTokenDto != null) { // 토큰 재발급 성공
			// RT 저장
			ResponseCookie responseCookie = ResponseCookie.from("refreshToken", reissuedTokenDto.getRefreshToken())
				.maxAge(COOKIE_EXPIRATION)
				.httpOnly(true)
				.secure(true)
				.build();
			SuccessResponse<?> successResponse = SuccessResponse.from(ResponseStatus.SUCCESS, tokens);
			tokens.put("accessToken", reissuedTokenDto.getAccessToken());
			tokens.put("refreshToken", reissuedTokenDto.getRefreshToken());

			return ResponseEntity
				.status(ResponseStatus.SUCCESS.getCode())
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
				.body(successResponse);
		} else {
			SuccessResponse<?> failResponse = SuccessResponse.from(ResponseStatus.TOKEN_UNAUTHORIZED, tokens);
			ResponseCookie responseCookie = ResponseCookie.from("refreshToken", "")
				.maxAge(0)
				.path("/")
				.build();
			return ResponseEntity
				.status(ResponseStatus.TOKEN_UNAUTHORIZED.getCode())
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(failResponse);
		}
	}

	// 로그아웃
	@PostMapping("/v1/logout")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
		String tokenMessage = "로그아웃 되었습니다.";
		SuccessResponse<?> successResponse = SuccessResponse.from(ResponseStatus.SUCCESS,tokenMessage);
		authService.logout(requestAccessToken);
		ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
			.maxAge(0)
			.path("/")
			.build();
		return ResponseEntity
			.status(ResponseStatus.SUCCESS.getCode())
			.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
			.body(successResponse);
	}
	@Override
	public ResponseEntity<?> name(@RequestHeader("Authorization") String requestAccessToken) {
		String loginType = jwtTokenProvider.getLoginType(requestAccessToken);
		String email = userService.getUserEmail();
		String userName;
		if ("Normal".equals(loginType)) {
			userName = userService.getUserName(email);
		} else {
			userName = userService.getOauthUserName(email);
		}
		SuccessResponse<?> successResponse = SuccessResponse.from(ResponseStatus.SUCCESS, userName);

		return ResponseEntity
			.status(ResponseStatus.SUCCESS.getCode())
			.body(successResponse);
	}

	@Override
	public ResponseEntity<?> validateJoinByEmail(@RequestHeader("Authorization") String requestAccessToken) {
		userService.checkEmailInDB(userService.getUserEmail());
		SuccessResponse<?> successResponse = SuccessResponse.from(ResponseStatus.SUCCESS, "이메일 인증 성공");
		return ResponseEntity
			.status(ResponseStatus.SUCCESS.getCode())
			.body(successResponse);
	}

}
