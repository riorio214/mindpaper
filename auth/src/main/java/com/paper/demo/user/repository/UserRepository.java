package com.paper.demo.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.paper.demo.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	@Query("SELECT u.name FROM users u WHERE u.email = :email")
	String findUserNameByEmail(String email);

	@Query("SELECT o.name FROM oauthusers o WHERE o.email = :email")
	String findOAuthUserNameByEmail(String email);
}
