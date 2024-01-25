package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.service.SchoolService;
import com.school.sba.userDTO.SchoolRequest;
import com.school.sba.userDTO.SchoolResponse;
import com.school.sba.userDTO.UserResponse;
import com.school.sba.util.ResponseStructure;


@RestController
public class SchoolController {
	
	@Autowired
	private SchoolService schoolServe;
	
	@PostMapping("/users/schools")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(@RequestBody SchoolRequest request){
		return schoolServe.createSchool(request);
	}
	
	@PutMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(@PathVariable int schoolId, @RequestBody SchoolRequest request){
		return schoolServe.updateSchool(schoolId,request);
	}
	
	@DeleteMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(@PathVariable int schoolId){
		return schoolServe.deleteSchool(schoolId);
	}
}
