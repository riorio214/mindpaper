package com.paper.demo.user.oauth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paper.demo.auth.jwt.JwtTokenProvider;
import com.paper.demo.auth.service.dto.AuthDto;
import com.paper.demo.common.ResponseStatus;
import com.paper.demo.common.SuccessResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OauthController {
	@GetMapping("/oauth/token")
	public ResponseEntity<?> Oauth2Login(@AuthenticationPrincipal HttpServletRequest httpServletRequest){
		HttpSession session = httpServletRequest.getSession();
		Map<String, String> tokens = new HashMap<>();
		tokens.put("accessToken", (String) session.getAttribute("accessToken"));
		tokens.put("refreshToken", (String) session.getAttribute("refreshToken"));
		// SuccessResponse 객체 생성
		SuccessResponse<?> successResponse = SuccessResponse.from(ResponseStatus.SUCCESS, tokens);
		// ResponseEntity에 SuccessResponse 및 쿠키, 헤더 추가하여 반환
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(successResponse);
	}

}
