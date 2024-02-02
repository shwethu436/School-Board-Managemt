package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class SchoolCannotBeCreatedException extends RuntimeException {
	private String message;

}
