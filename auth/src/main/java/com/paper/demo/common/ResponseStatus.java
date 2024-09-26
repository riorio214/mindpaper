package com.paper.demo.common;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum ResponseStatus {
	SUCCESS(true, 200, "성공"),
	INTERNAL_SERVER_ERROR(false, 500, "내부 서버의 오류가 발생했습니다."),
	//인증과인가, 요청처리
	//400 BAD_REQUEST 잘못된 요청
	DESERIALIZATION_ERROR(false, 400, "JSON 형식이 아닙니다."),
	//401 UNAUTHORIZED 인증 실패
	TOKEN_UNAUTHORIZED(false, 401, "인증에 실패하였습니다."),
	//403 FORBIDDEN 권한 없음
	ADMIN_ONLY(false, 403, "관리자만 접근 가능합니다."),

	//404 NOT_FOUND 잘못된 리소스 접근
	DISPLAY_NOT_FOUND(false, 404, "페이지 오류입니다."),
	//503 SERVICE_UNAVAILABLE 서비스 이용 불가
	SERVICE_UNAVAILABLE(false, 503, "서비스 이용이 불가합니다.");

	private final boolean isSuccess;
	private final int code;
	private final String message;

	ResponseStatus(boolean isSuccess, int code, String message) {
		this.isSuccess = isSuccess;
		this.code = code;
		this.message = message;
	}
}
