package com.paper.demo.user.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	private String email; // Principal

	private String password; // Credential

	private String name; // 사용자 이름

	@CreationTimestamp
	private Timestamp created;	// 생성일

	@UpdateTimestamp
	private Timestamp updated;	// 수정일

	@Enumerated(EnumType.STRING)
	private Role role; // 사용자 권한

	// == 생성 메서드 == //
	public static User registerUser(String email, String encodedPassword,String name ,Role role) {
		User user = new User();
		user.email = email;
		user.password = encodedPassword;
		user.name = name;
		user.role = role;
		return user;
	}
	public static User registerAdmin(String email, String encodedPassword,String name ,Role role) {
		User user = new User();
		user.email = email;
		user.password = encodedPassword;
		user.name = name;
		user.role = role;
		return user;
	}
}