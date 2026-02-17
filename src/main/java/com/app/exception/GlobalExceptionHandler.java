package com.app.exception;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationException(
			MethodArgumentNotValidException ex
			){
		Map<String, String> fieldErrors = new HashMap<>();
		
		ex.getBindingResult()
		.getFieldErrors()
		.forEach(error ->
		fieldErrors.put(error.getField(), error.getDefaultMessage())
		);
		
		ApiErrorResponse response = ApiErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.message("Validation Failed")
				.errors(fieldErrors)
				.timestamp(LocalDateTime.now())
				.build();
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleEntityNotFound(
			EntityNotFoundException ex
			){
		ApiErrorResponse response = ApiErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND.value())
				.message(ex.getMessage())
				.timestamp(LocalDateTime.now())
				.build();
		
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex){
		 
		ApiErrorResponse response = ApiErrorResponse.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message("Something went wrong")
				.timestamp(LocalDateTime.now())
				.build();
		
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InvalidDateRangeException.class)
	public ResponseEntity<?> handleInvalidDateRangeException(InvalidDateRangeException ex){
		
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

}
