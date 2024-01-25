package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.service.SubjectService;
import com.school.sba.userDTO.AcademicResponse;
import com.school.sba.userDTO.SubjectRequest;
import com.school.sba.userDTO.SubjectResponse;
import com.school.sba.util.ResponseStructure;

@RestController
public class SubjectController {
	@Autowired
	private SubjectService subjectServe;
	
	@PostMapping("/academicPrograms/{programId}/subjects")
	public ResponseEntity<ResponseStructure<AcademicResponse>> addSubject(@PathVariable int programId, @RequestBody SubjectRequest request){
		return subjectServe.addSubject(programId, request);
	}
	
	

}
