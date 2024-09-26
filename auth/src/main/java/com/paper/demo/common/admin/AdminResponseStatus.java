package com.paper.demo.common.admin;

import lombok.Getter;

@Getter
public enum AdminResponseStatus {

	//인증/인가, 요청처리
	//400 BAD_REQUEST 잘못된 요청
	PASSWORD_ERROR(false, 400, "비밀번호는 숫자, 문자, 특수문자를 포함하여 10자 이상이어야 합니다."),
	//401 UNAUTHORIZED 인증 실패
	TOKEN_UNAUTHORIZED(false, 401, "인증에 실패하였습니다."),
	//403 FORBIDDEN 권한 없음
	ADMIN_ONLY(false, 403, "관리자만 접근 가능합니다.");


	private final boolean isSuccess;
	private final int code;
	private final String message;

	AdminResponseStatus(boolean isSuccess, int code, String message) {
		this.isSuccess = isSuccess;
		this.code = code;
		this.message = message;
	}
}
