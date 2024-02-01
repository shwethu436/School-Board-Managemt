package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminCanNotAssignedToAcademicProgram extends RuntimeException {
	private String message;

}
