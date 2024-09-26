package com.paper.demo.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.paper.demo.auth.domain.JsonResponse;
import com.paper.demo.auth.domain.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService implements ISecurityServiceV1{

	@Autowired
	private final WebClient webClient;

	/**
	 * @apiNote 현재 인증된 사용자의 이메일을 반환한다.
	 * @return
	 */
	@Override
	public String getUserEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
			throw new RuntimeException("인증정보가 없거나 잘못된 토큰정보 입니다.");
		}
		return (String) jwt.getClaims().get("email");
	}



	/**
	 * @apiNote 인증서버에 로그인 요청
	 * @param loginDto
	 * @return
	 */
	public Mono<ResponseEntity<?>> login(UserDto.LoginDto loginDto){
		return this.webClient.post()
			.uri("/v1/login")
			.bodyValue(loginDto)
			.retrieve()
			.bodyToMono(JsonResponse.class)
			.map(response -> ResponseEntity
				.status(response.getCode())
				.body(response));
	}
	/**
	 * @apiNote 현재 인증된 사용자를 로그아웃 한다.
	 * @param AccessToken
	 * @retur
	 */
	public Mono<ResponseEntity<?>> logout(String AccessToken) {
		return this.webClient.post()
			.uri("/v1/logout")
			.header(HttpHeaders.AUTHORIZATION,AccessToken)
			.retrieve()
			.bodyToMono(JsonResponse.class)
			.map(response -> ResponseEntity
				.status(response.getCode())
				.body(response));
	}

	// 컨트롤러에서 로그아웃 메서드를 호출 한다.
	// 서비스에서는 logout 메서드를 호출하여 인증서버에 로그아웃을 요청한다.
	// 인증서버에서 반환된 메세지를 클라이언트에게 반환한다.

	public Mono<ResponseEntity<?>> signup(UserDto.SignUpDto signUpDto){
		return this.webClient.post()
			.uri("/v1/signup")
			.bodyValue(signUpDto)
			.retrieve()
			.bodyToMono(JsonResponse.class)
			.map(response -> ResponseEntity
				.status(response.getCode())
				.body(response));
	}

	public Mono<ResponseEntity<?>> signupAdmin(UserDto.SignUpDto signUpDto){
		return this.webClient.post()
			.uri("/v1/signup/inha")
			.bodyValue(signUpDto)
			.retrieve()
			.bodyToMono(JsonResponse.class)
			.map(response -> ResponseEntity
				.status(response.getCode())
				.body(response));
	}
	public Mono<ResponseEntity<?>> validateToken(String accessToken) {
		return this.webClient.post()
			.uri("/v1/validate")
			.header(HttpHeaders.AUTHORIZATION,accessToken)
			.retrieve()
			.bodyToMono(JsonResponse.class)
			.map(response -> ResponseEntity
				.status(response.getCode())
				.body(response));
	}
}
