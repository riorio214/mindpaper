package com.paper.demo.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class LoginDto {
		private String email;
		private String password;


		@Builder
		public LoginDto(String email, String password) {
			this.email = email;
			this.password = password;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SignUpDto {
		private String email;
		private String password;
		private String name;

		@Builder
		public SignUpDto(String email, String password,String name) {
			this.email = email;
			this.password = password;
			this.name = name;
		}
	}
}
