package com.app.exception;

public class InvalidDateRangeException extends RuntimeException{
	
	public InvalidDateRangeException() {
		super("Start Date cannot be after End Date");
	}
	
	public InvalidDateRangeException(String message) {
		super(message);
	}

}
