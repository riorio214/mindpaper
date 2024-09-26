package com.paper.demo.auth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class JsonResponse {
	private String timestamp;
	private boolean isSuccess;
	private int code;
	private String message;
	private Object data;

	@Getter
	public static class Token {
		@JsonProperty
		private String accessToken;
		@JsonProperty
		private String refreshToken;

		public Token(String accessToken, String refreshToken) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}
	}
	@Getter
	public static class JsonMessage{
		@JsonProperty
		private int code;
		@JsonProperty
		private String message;
		@JsonProperty
		private Object data;

		public JsonMessage(int code, String message, Object data) {
			this.code = code;
			this.message = message;
			this.data = data;
		}
	}

}
