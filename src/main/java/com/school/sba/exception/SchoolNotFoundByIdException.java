package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SchoolNotFoundByIdException extends RuntimeException {
	
	private String message;

}
