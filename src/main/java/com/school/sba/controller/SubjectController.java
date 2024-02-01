package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/academicPrograms/{programId}/subjects")
	public ResponseEntity<ResponseStructure<AcademicResponse>> addSubject(@PathVariable int programId, @RequestBody SubjectRequest request){
		return subjectServe.addSubject(programId, request);
	}
	
	@GetMapping("/subjects")
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> findAllSubjects(){
		return subjectServe.findAllSubjects();
	}

}
