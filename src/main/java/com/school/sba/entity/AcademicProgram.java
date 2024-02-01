package com.school.sba.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

import com.school.sba.enums.ProgramType;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcademicProgram {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int programId;
	private String programName;
	private LocalDate startsAt;
	private LocalDate endsAt;
	private ProgramType programType;
	
	private boolean isDeleted;
	
	@ManyToOne
	private School school;
	
	@ManyToMany
	private List<Subject> subjects;
	
	@ManyToMany
	private List<User> users;
	
	@OneToMany(mappedBy = "aList", fetch = FetchType.EAGER)
	private List<ClassHour> classHours;

}
