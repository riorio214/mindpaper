package com.paper.demo.user.oauth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paper.demo.user.oauth.domain.OauthUser;

public interface OAuthUserRepository extends JpaRepository<OauthUser,Long> {
	Optional<OauthUser> findByEmail(String email);
}
