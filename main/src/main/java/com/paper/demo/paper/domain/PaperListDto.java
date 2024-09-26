package com.paper.demo.paper.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaperListDto {
	/**
	 * PaperListDto
	 * 외부 클래스를 이용한 PaperListDto 생성
	 */
		private Long id;
		private String content;

		public PaperListDto(Long id, String content) {
			this.id = id;
			this.content = content;
		}

}
