package com.paper.demo.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.paper.demo.auth.domain.UserDto;
import com.paper.demo.auth.service.SecurityService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class SecurityController implements ISecurityControllerV1{
@Autowired
	private SecurityService securityService;


	@Override
	public Mono<ResponseEntity<?>> logout(@RequestHeader("Authorization") String AccessToken) {
		return securityService.logout(AccessToken);
	}

	@Override
	public Mono<ResponseEntity<?>> login(@RequestBody UserDto.LoginDto loginDto) {
		return securityService.login(loginDto);
	}

	@Override
	public Mono<ResponseEntity<?>> signupUser(@RequestBody UserDto.SignUpDto signUpDto){
		return securityService.signup(signUpDto);
	}

	@Override
	public Mono<ResponseEntity<?>> signupAdmin(@RequestBody UserDto.SignUpDto signUpDto){
		return securityService.signupAdmin(signUpDto);
	}

	@Override
	public Mono<ResponseEntity<?>> validateToken(@RequestHeader("Authorization") String accessToken) {
		return securityService.validateToken(accessToken);
	}
}
