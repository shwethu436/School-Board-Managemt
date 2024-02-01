package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.service.AcademicProgramService;
import com.school.sba.userDTO.AcademicRequest;
import com.school.sba.userDTO.AcademicResponse;
import com.school.sba.util.ResponseStructure;

@RestController
public class AcademicProgramController {
	@Autowired
	private AcademicProgramService academicServe;
	
	@PostMapping("/schools/{schoolId}/academicPrograms")
	public ResponseEntity<ResponseStructure<AcademicResponse>> addProgram(@PathVariable int schoolId,@RequestBody AcademicRequest request){
		return academicServe.addProgram(schoolId, request);
	}
	
	@GetMapping("/schools/{schoolId}/academicPrograms")
	public ResponseEntity<ResponseStructure<List<AcademicResponse>>>  findProgram(@PathVariable int schoolId){
		return academicServe.findProgram(schoolId);
	}
	
	@DeleteMapping("/academicProgram/{programId}")
	public ResponseEntity<ResponseStructure<AcademicResponse>>  deleteProgram(@PathVariable int programId){
		return academicServe.deleteProgram(programId);
	}
}
