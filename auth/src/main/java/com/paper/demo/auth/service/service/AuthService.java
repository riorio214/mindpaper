package com.paper.demo.auth.service.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.paper.demo.auth.jwt.JwtTokenProvider;
import com.paper.demo.auth.service.dto.AuthDto;
import com.paper.demo.user.details.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthServiceV1 {
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final RedisService redisService;
	private final String SERVER = "Server";

	// 로그인: 인증 정보 저장 및 비어 토큰 발급
	public AuthDto.TokenDto login(AuthDto.LoginDto loginDto) throws NoSuchAlgorithmException, InvalidKeySpecException {
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
		Authentication authentication = authenticationManagerBuilder.getObject()
			.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return generateToken(SERVER, authentication.getName(), getAuthorities(authentication));
	}

	// AT가 만료일자만 초과한 유효한 토큰인지 검사
	public boolean validate(String requestAccessTokenInHeader) {
		String requestAccessToken = resolveToken(requestAccessTokenInHeader);
		return jwtTokenProvider.validateAccessTokenOnlyExpired(requestAccessToken); // true = 재발급
	}

	// 토큰 재발급: validate 메서드가 true 반환할 때만 사용 -> AT, RT 재발급
	public AuthDto.TokenDto reissue(String requestAccessTokenInHeader, String requestRefreshToken){
		String requestAccessToken = resolveToken(requestAccessTokenInHeader);
		Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);
		String principal = getPrincipal(requestAccessToken);
		String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
		if (refreshTokenInRedis == null) { // Redis에 저장되어 있는 RT가 없을 경우
			System.out.println("Redis에 저장된 RT가 없습니다.");
			return null; // -> 재로그인 요청
		}
		System.out.println("RT의 유효성 검사 : " + jwtTokenProvider.validateRefreshToken(requestRefreshToken));
		// 요청된 RT의 유효성 검사 & Redis에 저장되어 있는 RT와 같은지 비교
		if (jwtTokenProvider.validateRefreshToken(requestRefreshToken) || !Objects.equals(refreshTokenInRedis,
			requestRefreshToken)) {
			System.out.println("RT가 유효하지 않거나, Redis에 저장된 RT와 다릅니다.");
			redisService.deleteValues("RT(" + SERVER + "):" + principal); // 탈취 가능성 -> 삭제
			return null; // -> 재로그인 요청
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String authorities = getAuthorities(authentication);
		// 토큰 재발급 및 Redis 업데이트
		System.out.println("Redis에 저장된 RT: " + redisService.getValues("RT(" + SERVER + "):" + principal));
		redisService.deleteValues("RT(" + SERVER + "):" + principal); // 기존 RT 삭제
		AuthDto.TokenDto tokenDto = jwtTokenProvider.createToken(principal, authorities,"normal");
		saveRefreshToken(SERVER, principal, tokenDto.getRefreshToken());
		return tokenDto;
	}
	// 토큰 발급
	public AuthDto.TokenDto generateToken(String provider, String email, String authorities) {
		// RT가 이미 있을 경우
		if (redisService.getValues("RT(" + provider + "):" + email) != null) {
			redisService.deleteValues("RT(" + provider + "):" + email); // 삭제
		}
		// AT, RT 생성 및 Redis에 RT 저장
		AuthDto.TokenDto tokenDto = jwtTokenProvider.createToken(email, authorities,"normal");
		saveRefreshToken(provider, email, tokenDto.getRefreshToken());
		return tokenDto;
	}

	// RT를 Redis에 저장
	public void saveRefreshToken(String provider, String principal, String refreshToken) {
		redisService.setValuesWithTimeout("RT(" + provider + "):" + principal, // key
			refreshToken, // value
			jwtTokenProvider.getTokenExpirationTime(refreshToken)); // timeout(milliseconds)
	}

	// 권한 이름 가져오기
	public String getAuthorities(Authentication authentication) {
		return authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
	}

	// AT로부터 principal 추출
	public String getPrincipal(String requestAccessToken) {
		return jwtTokenProvider.getAuthentication(requestAccessToken).getName();
	}

	// "Bearer {AT}"에서 {AT} 추출
	public String resolveToken(String requestAccessTokenInHeader) {
		if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith("Bearer ")) {
			return requestAccessTokenInHeader.substring(7);
		}
		return null;
	}

	// 로그아웃
	public void logout(String requestAccessTokenInHeader) {
		String requestAccessToken = resolveToken(requestAccessTokenInHeader);
		String principal = getPrincipal(requestAccessToken);

		// Redis에 저장되어 있는 RT 삭제
		String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
		if (refreshTokenInRedis != null) {
			redisService.deleteValues("RT(" + SERVER + "):" + principal);
		}

		// Redis에 로그아웃 처리한 AT 저장
		long expiration = jwtTokenProvider.getTokenExpirationTime(requestAccessToken) - new Date().getTime();
		redisService.setValuesWithTimeout(requestAccessToken,
			"logout",
			expiration);
	}
}
