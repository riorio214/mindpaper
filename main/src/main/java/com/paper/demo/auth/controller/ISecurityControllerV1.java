package com.paper.demo.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.paper.demo.auth.domain.UserDto;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

public interface ISecurityControllerV1 {
	/**
	 * 로그아웃
	 * @param accessToken
	 * @return
	 */
	@Operation(summary = "로그아웃", description = "로그아웃을 수행합니다.")
	@Parameter(name = "Authorization", description = "Access Token", required = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그아웃 성공"),
			@ApiResponse(responseCode = "400", description = "로그아웃 실패")
	})
	@GetMapping("/v1/auth/logout")
	Mono<ResponseEntity<?>> logout(@RequestHeader("Authorization") String accessToken);

	/**
	 * 로그인
	 * @param loginDto
	 * @return
	 */
	@Operation(summary = "로그인", description = "로그인을 수행합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "400", description = "로그인 실패")
	})
	@GetMapping("/v1/auth/login")
	Mono<ResponseEntity<?>> login(@RequestBody UserDto.LoginDto loginDto);

	/**
	 * 회원가입
	 * @param signUpDto
	 * @return
	 */
	@Operation(summary = "회원가입", description = "회원가입을 수행합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "회원가입 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패"),
			@ApiResponse(responseCode = "400", description = "회원가입 실패")
	})
	@GetMapping("/v1/auth/signup")
	Mono<ResponseEntity<?>> signupUser(@RequestBody @Valid UserDto.SignUpDto signUpDto);

	/**
	 * 관리자 회원가입
	 * @param signUpDto
	 * @return
	 */
	@Operation(summary = "관리자 회원가입", description = "관리자 회원가입을 수행합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "회원가입 성공"),
			@ApiResponse(responseCode = "400", description = "회원가입 실패")
	})
	@GetMapping("/v1/auth/signup/inha")
	Mono<ResponseEntity<?>> signupAdmin(@RequestBody @Valid UserDto.SignUpDto signUpDto);

	/**
	 * 토큰 유효성 검사
	 * @param accessToken
	 * @return
	 */
	@Operation(summary = "토큰 유효성 검사", description = "토큰의 유효성을 검사합니다.")
	@Parameter(name = "Authorization", description = "Access Token", required = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "토큰 유효성 검사 성공"),
			@ApiResponse(responseCode = "400", description = "토큰 유효성 검사 실패")
	})
	@GetMapping("/v1/auth/validate")
	Mono<ResponseEntity<?>> validateToken(@RequestHeader("Authorization") String accessToken);
}
