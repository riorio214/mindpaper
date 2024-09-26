package com.paper.demo.user.oauth.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.paper.demo.auth.jwt.JwtTokenProvider;
import com.paper.demo.auth.service.dto.AuthDto;
import com.paper.demo.auth.service.service.AuthService;
import com.paper.demo.user.oauth.domain.OauthUser;
import com.paper.demo.user.oauth.domain.OAuthAttributes;
import com.paper.demo.user.oauth.domain.SessionUser;
import com.paper.demo.user.oauth.repository.OAuthUserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final OAuthUserRepository OAuthUserRepository;
	private final HttpSession httpSession;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthService authService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		//소셜에서 인증받아온 유저정보를 담고있다.
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		// OAuth2 서비스 id (구글, 카카오, 네이버)
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		// OAuth2 로그인 진행 시 키가 되는 필드 값(PK)
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		// OAuth2UserService
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
		OauthUser oauthUser = saveOrUpdate(attributes);
		httpSession.setAttribute("user", new SessionUser(oauthUser)); // SessionUser (직렬화된 dto 클래스 사용)
		// JWT 토큰 생성
		String authorities = oauthUser.getRoleKey();
		AuthDto.TokenDto tokens = jwtTokenProvider.createToken(oauthUser.getEmail(), authorities,oauthUser.getOauth());
		authService.saveRefreshToken("OauthServer",oauthUser.getEmail(), tokens.getRefreshToken());
		httpSession.setAttribute("accessToken", tokens.getAccessToken());
		httpSession.setAttribute("refreshToken", tokens.getRefreshToken());
		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(oauthUser.getRoleKey())),
			attributes.getAttributes(), attributes.getNameAttributeKey());
	}

	// 유저 생성 및 수정 서비스 로직
	private OauthUser saveOrUpdate(OAuthAttributes attributes){
		OauthUser oauthUser = OAuthUserRepository.findByEmail(attributes.getEmail())
			.map(entity -> entity.update(attributes.getName()))
			.orElse(attributes.toEntity());
		return OAuthUserRepository.save(oauthUser);
	}

}
