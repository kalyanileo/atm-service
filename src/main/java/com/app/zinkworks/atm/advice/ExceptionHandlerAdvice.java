package com.app.zinkworks.atm.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.zinkworks.atm.exception.AtmException;

import lombok.Data;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AtmException.class)
	@ResponseBody
	public ErrorResponse handle(final AtmException ex) {
		return new ErrorResponse(ex.getErrorCode(),ex.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ErrorResponse handle(final Exception ex) {
		return new ErrorResponse("-1", ex.getMessage());
	}
	
	@Data
	public static class ErrorResponse {
		private final String code;
		private final String message;
	}

}

