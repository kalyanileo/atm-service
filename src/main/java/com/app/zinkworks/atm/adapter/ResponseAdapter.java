package com.app.zinkworks.atm.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.zinkworks.atm.exception.AtmException;


@Service
public class ResponseAdapter {
	public <T> ResponseEntity<T> buildResponse(T response) {		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	public void throwException(HttpStatus status) throws AtmException {
		if (!HttpStatus.OK.equals(status))
			throw new AtmException("An unexpected internal server error occured");
	}

}
