package com.app.zinkworks.atm.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AtmException extends Exception {
	
public static final String DEFAULT_ERROR_CODE = "-1";
	
	private final String errorCode;
	
	public AtmException() {
		errorCode = DEFAULT_ERROR_CODE;		
	}
	
	public AtmException(String message) {
		super(message);		
		errorCode = DEFAULT_ERROR_CODE;
	}

}
