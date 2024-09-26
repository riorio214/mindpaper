package com.paper.demo.user.details;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.paper.demo.user.domain.User;
import com.paper.demo.user.oauth.domain.OauthUser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter

public class UserDetailsImpl implements UserDetails {

	private final User user;
	private final OauthUser oauthUser;

	// User 객체를 처리하는 기존 생성자
	public UserDetailsImpl(User user) {
		this.user = user;
		this.oauthUser = null;
	}

	// OauthUser 객체를 처리할 수 있는 새로운 생성자
	public UserDetailsImpl(OauthUser oauthUser) {
		this.oauthUser = oauthUser;
		this.user = null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();

		if (user != null) {
			authorities.add(() -> user.getRole().getKey()); // key: ROLE_권한
		} else if (oauthUser != null) {
			authorities.add(() -> oauthUser.getRole().getKey()); // key: ROLE_권한
		}

		return authorities;
	}

	@Override
	public String getUsername() {
		if (user != null) {
			return user.getEmail();
		} else if (oauthUser != null) {
			return oauthUser.getEmail();
		}
		return null; // 혹은 적절한 예외 처리
	}


	@Override
	public String getPassword() {
		if (user != null) {
			return user.getPassword();
		}
		return null; // 혹은 적절한 예외 처리
	}


	// == 세부 설정 == //

	@Override
	public boolean isAccountNonExpired() { // 계정의 만료 여부
		return true;
	}

	@Override
	public boolean isAccountNonLocked() { // 계정의 잠김 여부
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() { // 비밀번호 만료 여부
		return true;
	}

	@Override
	public boolean isEnabled() { // 계정의 활성화 여부
		return true;
	}
}
