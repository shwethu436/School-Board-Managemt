package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.userDTO.AcademicRequest;
import com.school.sba.userDTO.AcademicResponse;
import com.school.sba.util.ResponseStructure;

public interface AcademicProgramService {

	ResponseEntity<ResponseStructure<AcademicResponse>> addProgram(int schoolId, AcademicRequest request);

	ResponseEntity<ResponseStructure<AcademicResponse>> findProgram(int schoolId);

}
