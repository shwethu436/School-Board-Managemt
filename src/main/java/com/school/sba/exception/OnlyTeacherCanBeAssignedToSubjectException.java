package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class OnlyTeacherCanBeAssignedToSubjectException extends RuntimeException {
	private String message;

}
