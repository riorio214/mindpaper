package com.paper.demo.user.oauth.domain;

import java.util.Map;

import com.paper.demo.user.domain.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
	private final Map<String, Object> attributes;
	private final String nameAttributeKey;
	private final String name;
	private final String email;
	private final String oauth;
	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email,String oauth) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.oauth = oauth;
	}

	public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
		if("kakao".equals(registrationId)){
			return ofKakao("id",attributes);
		}else if("google".equals(registrationId)){
			return ofGoogle(userNameAttributeName, attributes);
		}else{
			return ofNaver("id", attributes);
		}
	}
	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.name((String) attributes.get("name"))
			.email((String) attributes.get("email"))
			.oauth("google")
			.attributes(attributes)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}
	private static OAuthAttributes ofNaver(String userNameAttributeName,
		Map<String, Object> attributes){
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");

		return OAuthAttributes.builder()
			.name((String) response.get("name"))
			.email((String) response.get("email"))
			.oauth("naver")
			.attributes(response)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}
	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

		return OAuthAttributes.builder()
			.name((String) kakaoProfile.get("nickname"))
			.email((String) kakaoAccount.get("email"))
			.oauth("kakao")
			.nameAttributeKey(userNameAttributeName)
			.attributes(attributes)
			.build();
	}

	public OauthUser toEntity() {
		return OauthUser.builder()
			.name(name)
			.email(email)
			.oauth(this.oauth)
			.role(Role.USER)
			.build();
	}
}
