package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentCanNotAssignedToAcademicProgram extends RuntimeException {
	private String message;

}
