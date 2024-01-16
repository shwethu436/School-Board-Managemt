package com.school.sba.entity;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int scheduleId;
	private LocalTime opensAt;
	private LocalTime closesAt;
	private LocalTime classHourLength;
	private LocalTime breakTime;
	private LocalTime breakLength;
	private LocalTime launchTime;
	private LocalTime launchLength;
	private int classHoursPerDay;
	
    
}
