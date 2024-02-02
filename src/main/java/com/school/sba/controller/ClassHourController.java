package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.service.ClassHourService;
import com.school.sba.userDTO.ClassHourRequest;
import com.school.sba.userDTO.ClassHourResponse;
import com.school.sba.util.ResponseStructure;

@RestController
public class ClassHourController {
	@Autowired
	private ClassHourService classHourServe;
	
	@PostMapping("/academicProgram/{programId}/classHours")
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(@PathVariable int programId ){
		return classHourServe.generateClassHour(programId);
	}
	
	@PutMapping("/class-hours")
	public ResponseEntity<String> updateClassHour(@RequestBody List<ClassHourRequest> request){
		classHourServe.updateClassHour(request);
		return ResponseEntity.ok("Class hours updated successfully");
	}
	
	@PostMapping("/academic-Program/{programId}/classHours")
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateNextWeekClassHour(@PathVariable int programId ){
		return classHourServe.generateNextWeekClassHour(programId);
	}

}
