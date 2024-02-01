package com.school.sba.userDTO;

import java.time.LocalDate;
import java.time.LocalTime;

import com.school.sba.enums.ProgramType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class AcademicRequest {
	
	private String programName;
	private LocalDate startsAt;
	private LocalDate endsAt;
	private ProgramType programType;

}
