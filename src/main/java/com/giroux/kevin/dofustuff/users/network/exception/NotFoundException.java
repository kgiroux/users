package com.giroux.kevin.dofustuff.users.network.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

	/**
	 *  serialVersionUID : 
	 */
	private static final long serialVersionUID = -8627507957763574502L;

	
	public NotFoundException() {
		
	}
	
	public NotFoundException(final String message) {
		super(message);
	}
}
