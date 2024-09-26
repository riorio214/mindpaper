package com.paper.demo.paper.domain;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageIdDto {
		private String email;
		private String title;
		private List<String> content;
		private String loginType;


		public PageIdDto(String email, String title, List<String> content, String loginType) {
			this.email = email;
			this.title = title;
			this.content = content;
			this.loginType = loginType;
		}
}
