package com.paper.demo.common.main;

import lombok.Getter;

@Getter
public enum MainResponseStatus {

	//롤링페이퍼 관련
	PAPER_CREATED(true, 201, "페이퍼 생성에 성공하였습니다."),
	PAPER_NOT_FOUND(false, 500, "페이퍼를 찾을 수 없습니다."),
	PAPER_CREATION_FAILED(false, 500, "페이퍼를 생성할 수 없습니다."),

	CONNECTION_FAILED(false, 404, "통신에 실패했습니다.");

	private final boolean isSuccess;
	private final int code;
	private final String message;

	MainResponseStatus(boolean isSuccess, int code, String message) {
		this.isSuccess = isSuccess;
		this.code = code;
		this.message = message;
	}
}
