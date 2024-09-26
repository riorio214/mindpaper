package com.paper.demo.auth.service;

import org.springframework.http.ResponseEntity;

import com.paper.demo.auth.domain.UserDto;

import reactor.core.publisher.Mono;

public interface ISecurityServiceV1{


	String getUserEmail();

	Mono<ResponseEntity<?>> logout(String accessToken);
	Mono<ResponseEntity<?>> login(UserDto.LoginDto loginDto);

	Mono<ResponseEntity<?>> signup(UserDto.SignUpDto signUpDto);
	Mono<ResponseEntity<?>> signupAdmin(UserDto.SignUpDto signUpDto);
	Mono<ResponseEntity<?>> validateToken(String accessToken);
}
