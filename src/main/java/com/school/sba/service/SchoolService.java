package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.userDTO.SchoolRequest;
import com.school.sba.userDTO.SchoolResponse;
import com.school.sba.util.ResponseStructure;

public interface SchoolService {
	
	

	

	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(SchoolRequest request);

	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(int schoolId, SchoolRequest request);

	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId);

}
