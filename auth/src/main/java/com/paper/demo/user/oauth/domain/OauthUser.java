package com.paper.demo.user.oauth.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.paper.demo.user.domain.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "oauthusers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	private String email; // Principal

	private String name; // 사용자 이름

	@CreationTimestamp
	private Timestamp created;	// 생성일

	@UpdateTimestamp
	private Timestamp updated;	// 수정일

	private String oauth; // 소셜 로그인 종류

	@Enumerated(EnumType.STRING)
	private Role role;


	@Builder
	public OauthUser(Long id,String name,String email,String oauth,Role role){
		this.id = id;
		this.name = name;
		this.email = email;
		this.oauth = oauth;
		this.role = role;
	}

	public OauthUser update(String name){
		this. name = name;
		return this;

	}
	public String getRoleKey(){
		return this.role.getKey();
	}

}
