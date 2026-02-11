package com.app.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiErrorResponse {
	private int status;
	private String message;
	private LocalDateTime timestamp;
	private Map<String, String> errors;

}
