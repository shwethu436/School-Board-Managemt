package com.school.sba.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import com.school.sba.enums.UserRole;

import jakarta.persistence.Column;
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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Component
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column(unique = true)
	private String userName;
	
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	private long contactNo;
	
	@Column(unique = true)
	private String UserEmail;
	private UserRole userRole;
	
	 private boolean isDeleted;
	 
	 @ManyToOne
	 private School school;
	 
	 @ManyToMany(mappedBy = "users")
	 private List<AcademicProgram> academicPrograms;
	 
	 @ManyToOne
	 private Subject subject;
	 
	 @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	 private List<ClassHour> classHours;

}
