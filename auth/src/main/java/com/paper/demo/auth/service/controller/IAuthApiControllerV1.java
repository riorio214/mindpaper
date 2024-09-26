package com.paper.demo.auth.service.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.paper.demo.auth.service.dto.AuthDto;
import com.paper.demo.common.admin.AdminException;

import jakarta.validation.Valid;

public interface IAuthApiControllerV1 {

	/**
	 * RT = Refresh Token, AT = Access Token
	 * 1. 회원가입
	 * 2. 로그인 -> 토큰 발급
	 * 3. 토큰 재발급
	 * 4. 로그아웃
	 * 회원가입은 localhost/auth/v1/signup
	 * Body Email,Password -> Json 형태로 전달
	 *
	 */
	@Operation(summary = "회원가입", description = "회원가입을 수행합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "회원가입 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패"),
			@ApiResponse(responseCode = "400", description = "회원가입 실패")
	})
	@PostMapping("/v1/signup")
	ResponseEntity<?> signupUser(@RequestBody @Valid AuthDto.SignupDto signupDto) throws AdminException;
	// 관리자 회원가입
	@Operation(summary = "관리자 회원가입", description = "회원가입을 수행합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "회원가입 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패"),
			@ApiResponse(responseCode = "400", description = "회원가입 실패")
	})
	@PostMapping("/v1/signup/inha")
	ResponseEntity<?> signupAdmin(@RequestBody @Valid AuthDto.SignupDto signupDto);
	/**
	 * 로그인
	 * @param loginDto
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	@Operation(summary = "로그인", description = "로그인을 수행합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "400", description = "로그인 실패")
	})
	@PostMapping("/v1/login")
	ResponseEntity<?> login(@RequestBody @Valid AuthDto.LoginDto loginDto) throws
		NoSuchAlgorithmException,
		InvalidKeySpecException;

	/**
	 * 토큰 재발급
	 * @param requestRefreshToken
	 * @param requestAccessToken
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	@Operation(summary = "토큰 재발급", description = "토큰 재발급을 수행합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
			@ApiResponse(responseCode = "400", description = "토큰 재발급 실패")
	})
	@PostMapping("/v1/reissue")
	ResponseEntity<?> reissue(@CookieValue(name = "refreshToken") String requestRefreshToken,
		@RequestHeader("Authorization") String requestAccessToken) throws
		NoSuchAlgorithmException,
		InvalidKeySpecException;

	/**
	 * 토큰 검증
	 * @param requestAccessToken
	 * @return
	 */
	@Operation(summary = "토큰 검증", description = "토큰의 유효성을 검사합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "토큰 검증 성공"),
			@ApiResponse(responseCode = "400", description = "토큰 검증 실패")
	})
	@PostMapping("/v1/validate")
	ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken);
	/**
	 * 로그아웃
	 * @param requestAccessToken
	 * @return
	 */
	@Operation(summary = "로그아웃", description = "로그아웃을 수행합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그아웃 성공"),
			@ApiResponse(responseCode = "400", description = "로그아웃 실패")
	})
	@PostMapping("/v1/logout")
	ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken);

	// @Operation(summary = "이메일 인증", description = "이메일 인증을 수행합니다.")
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
	// 		@ApiResponse(responseCode = "400", description = "이메일 인증 실패")
	// })
	// @PostMapping("/v1/email")
	// ResponseEntity<?> email(@RequestBody @Valid AuthDto.EmailDto emailDto) throws AdminException;

	@GetMapping("/v1/name")
	ResponseEntity<?> name(@RequestHeader("Authorization") String requestAccessToken);

	@GetMapping("/v1/validate/join")
	ResponseEntity<?> validateJoinByEmail(@RequestHeader("Authorization") String requestAccessToken);
}
