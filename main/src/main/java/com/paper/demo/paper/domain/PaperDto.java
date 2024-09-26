package com.paper.demo.paper.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaperDto {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class createPaper{
		private String content;
		public createPaper(String content) {
			this.content = content;
		}
	}


	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class createPage{
		private String email;
		private String title;

		public createPage(String email, String title) {
			this.email = email;
			this.title = title;
		}
	}



}
