package com.paper.demo.common.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainException extends RuntimeException {
	private MainResponseStatus status;
}
