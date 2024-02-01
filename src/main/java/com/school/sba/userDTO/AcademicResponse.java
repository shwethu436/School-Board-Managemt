package com.school.sba.userDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.school.sba.entity.Subject;
import com.school.sba.enums.ProgramType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@Builder
@Setter
@Getter
public class AcademicResponse {
	private int programId;
	private String programName;
	private LocalDate startsAt;
	private LocalDate endsAt;
	private ProgramType programType;
	private List<Subject> subjects;

}
