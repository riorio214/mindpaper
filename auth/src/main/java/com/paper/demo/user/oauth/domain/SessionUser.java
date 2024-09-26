package com.paper.demo.user.oauth.domain;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable {
	private final String name;
	private final String email;

	public SessionUser(OauthUser oauthUser){
		this.name = oauthUser.getName();
		this.email = oauthUser.getEmail();
	}
}
