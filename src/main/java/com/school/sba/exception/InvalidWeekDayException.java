package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class InvalidWeekDayException extends RuntimeException {
	private String message;

}
