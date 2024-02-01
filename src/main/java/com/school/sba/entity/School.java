package com.school.sba.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Setter
@Getter

public class School {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int schoolId;
	private String schoolName;
	private String schoolEmail;
	private String schoolAddress;
	private long schoolContact;
	
	private boolean isDeleted;
	
	@OneToOne
	private Schedule schedule;
	
	@OneToMany(mappedBy = "school", fetch = FetchType.EAGER)
	private List<AcademicProgram> aList;
	
	@OneToMany(mappedBy = "school", fetch = FetchType.EAGER)
	private List<User> uList;
	
	

}
