package com.paper.demo.user.details;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.paper.demo.user.domain.User;
import com.paper.demo.user.oauth.domain.OauthUser;
import com.paper.demo.user.oauth.repository.OAuthUserRepository;
import com.paper.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	private final OAuthUserRepository oAuthUserRepository;

	@Override
	public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			// userRepository에서 유저를 찾습니다.
			User findUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Can't find user with this email. -> " + email)); // User를 찾지 못한 경우 UsernameNotFoundException을 던집니다.
			return new UserDetailsImpl(findUser);
		} catch (UsernameNotFoundException e) {
			// userRepository에서 유저를 찾지 못한 경우, oAuthUserRepository에서 시도합니다.
			OauthUser findOauthUser = oAuthUserRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Can't find user with this email. -> " + email)); // OauthUser를 찾지 못한 경우 UsernameNotFoundException을 던집니다.
			return new UserDetailsImpl(findOauthUser);
		}
	}


}
